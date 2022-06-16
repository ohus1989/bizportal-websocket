package com.kdax.bizportal.common.util;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class MaskingUtil {

    public String maskingPersonalInfo(MaskingType type, String value) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }

        value = value.trim();

        String str = "";
        switch (type) {
            case PHONE:
                str = phoneMasking(value);
                break;
            case E_MAIL:
                str = emailMasking(value);
                break;
            case ADDRESS_DETAIL:
                str = addressMasking(value);
                break;
            case REGISTRATION_NUMBER:
                str = registrationNoMasking(value);
                break;
            case ACCOUNT_NO:
                str = accountNoMasking(value);
                break;
            default:
                break;
        }
        return str;
    }


    /**
     * @param phoneNumber
     * @return
     * @throws Exception
     * @desc 핸드폰번호 마스킹
     */
    private String phoneMasking(String phoneNumber) {
        String regex = "^[0-9]*$";
        Matcher matcher = Pattern.compile(regex).matcher(phoneNumber);
        if (matcher.find()) {
            String target = matcher.group(0).substring(3,7);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');
            return phoneNumber.replace(target, String.valueOf(c));
        }
        return phoneNumber;
    }

    /**
     * @param email
     * @return
     * @throws Exception
     * @desc 이메일 마스킹 (앞 3자리 표현 후 '@'전까지 마스킹)
     */
    private String emailMasking(String email) {
        String regex = "^[_0-9a-zA-Z-]+@[0-9a-zA-Z-]+(.[_0-9a-zA-Z-]+)*$";
        Matcher matcher = Pattern.compile(regex).matcher(email);
        if (matcher.find()) {
            String target = matcher.group(0);
            target = target.split("@")[0];
            int length = target.length();
            if (length > 3) {
                char[] c = new char[length - 3];
                Arrays.fill(c, '*');
                return email.replace(target, target.substring(0, 3) + String.valueOf(c));
            }
        }
        return email;
    }

    /**
     * @param address
     * @return
     * @throws Exception
     * @desc 주소 마스킹(신주소, 구주소, 도로명 주소 숫자만 전부 마스킹)
     */
    private String addressMasking(String address) {
        // 신(구)주소, 도로명 주소 S
        String regex = "(([가-힣]+(\\d{1,5}|\\d{1,5}(,|.)\\d{1,5}|)+(읍|면|동|가|리))(^구|)((\\d{1,5}(~|-)\\d{1,5}|\\d{1,5})(가|리|)|))([ ](산(\\d{1,5}(~|-)\\d{1,5}|\\d{1,5}))|)|";
        String newRegx = "(([가-힣]|(\\d{1,5}(~|-)\\d{1,5})|\\d{1,5})+(로|길))";
        Matcher matcher = Pattern.compile(regex).matcher(address);
        Matcher newMatcher = Pattern.compile(newRegx).matcher(address);
        if (matcher.find()) {
            return address.replaceAll("[0-9]", "*");
        } else if (newMatcher.find()) {
            return address.replaceAll("[0-9]", "*");
        }
        return address;
    }


    /**
     * @param registrationNo
     * @return
     * @throws Exception
     * @desc 주민등록번호 마스킹 마지막 6자리
     */
    private String registrationNoMasking(String registrationNo) {
        String regex = "^(\\d{6}\\D?\\d{1})(\\d{6})$";
        String regexDrive = "^(.*)[0-9]{2}[-~.[:space:]][0-9]{6}[-~.[:space:]][0-9]{2}(.*)$";
        String regexForeign = "^(.*)([01][0-9]{5}[[:space:]~-]+[1-8][0-9]{6}|[2-9][0-9]{5}[[:space:]~-]+[56][0-9]{6})(.*)$";
        Matcher matcher = Pattern.compile(regex).matcher(registrationNo);
        Matcher matcherDrive = Pattern.compile(regexDrive).matcher(registrationNo);
        Matcher matcherForeign = Pattern.compile(regexForeign).matcher(registrationNo);

        if (matcher.find()) {
            return new StringBuffer(matcher.group(1)).append("******").toString();
        }else if(matcherDrive.find()){
            return new StringBuffer(matcherDrive.group(1)).append("******").toString();
        }else if(matcherForeign.find()){
            return new StringBuffer(matcherForeign.group(1)).append("******").toString();
        }
        return registrationNo;
    }

    /**
     *
     * @param accountNo
     * @return
     * @desc 계좌번호 마스킹 마지막 5자리
     */
    private String accountNoMasking(String accountNo){
        String regex = "^(\\d{1,})(-(\\d{1,})){1,}";
        Matcher matcher = Pattern.compile(regex).matcher(accountNo);
        if (matcher.find()) {
            int length = accountNo.length();
            if (length > 5) {
                char[] c = new char[5];
                Arrays.fill(c, '*');
                return accountNo.replace(accountNo, accountNo.substring(0, length - 5) + String.valueOf(c));
            }
        }
        return accountNo;
    }

}
