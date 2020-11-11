package com.kdax.bizportal.common.voCommon;

import com.kdax.bizportal.common.exception.BizExceptionMessage;
import lombok.Data;

@Data
public class LogComVO {
    LogReqVO request;
    LogResVO response;
    StackTraceElement traceElement;
    BizExceptionMessage bizExceptionMessage;
}
