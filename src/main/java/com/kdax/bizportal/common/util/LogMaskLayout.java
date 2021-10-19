package com.kdax.bizportal.common.util;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import com.google.gson.Gson;
import com.kdax.bizportal.common.voCommon.LogMaskVO;
import org.slf4j.MDC;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LogMaskLayout extends PatternLayout {

    private Pattern multilinePattern;
//    private List<String> maskPatterns = new ArrayList<>();

    private LogMaskVO logMaskVO;

    public void addMaskPattern(String maskPattern) {
//        maskPatterns.add(maskPattern);
//        multilinePattern = Pattern.compile(maskPatterns
//                                                   .stream()
//                                                   .collect(Collectors.joining("|")), Pattern.MULTILINE);
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        return maskMessage(super.doLayout(event));
    }

    @Override public Map<String, String> getDefaultConverterMap() {
        return super.getDefaultConverterMap();
    }

    @Override public void start() {
        super.start();
    }

    @Override public void setPattern(String pattern) {
        super.setPattern(pattern);
    }

    @Override public void setContext(Context context) {
        super.setContext(context);
    }


    private String maskMessage(String message) {

        String bizPatternMask = MDC.get("logMask");
//        String bizPatternMask = ThreadContext.get("bizPatternMask");
        try {
            if (bizPatternMask != null) {
                logMaskVO = new Gson().fromJson(bizPatternMask, LogMaskVO.class);
                multilinePattern = Pattern.compile(logMaskVO
                                                           .getLogMaskVOItem()
                                                           .stream()
                                                           .map(LogMaskVO.LogMaskVOItem::getPattern)
                                                           .collect(Collectors.joining("|")), Pattern.MULTILINE);
            }

//        System.out.println("ThreadContext : " + bizPatternMask);
            if (multilinePattern == null) {
                return message;
            }

            StringBuffer sb = new StringBuffer(message);
            Matcher matcher = multilinePattern.matcher(sb);
            while (matcher.find()) {
                int bound = matcher.groupCount();
                for (int group = 1; group <= bound; group++) {
                    if (matcher.group(group) != null) {

                        LogMaskVO.LogMaskVOItem logMaskVOItem = logMaskVO
                                .getLogMaskVOItem()
                                .get(group - 1);

                        final Pattern pattern = Pattern.compile(logMaskVOItem.getRegex(), Pattern.MULTILINE);
                        final Matcher matcher2 = pattern.matcher(message.substring(matcher.start(group),
                                                                                   matcher.end(group)
                        ));

                        sb.replace(matcher.start(group),
                                   matcher.end(group),
                                   matcher2.replaceAll(logMaskVOItem.getSubst())
                        );
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.printf("LogMask Error %s%n", e);
            return message;
        }
    }
}