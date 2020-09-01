package com.kdax.bizportal.common.exception;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

public enum ErrorType {
    ALL_SUCCESS_OK(0, "OK.", "all thing sucess.", "ALL_SUCCESS_OK"),

    // 21000 ~  Sing In Message
    DUPLICATION_USER_ERROR(21001, "중복된 사용자입니다.", "duplicate user..", "DUPLICATION_USER_ERROR"),
    NOT_FOUND_USER_ERROR(21002, "유효하지 않은 사용자입니다.", "not found user in database", "NOT_FOUND_USER_ERROR"),
    INPUT_ERROR(21003, "유효하지 않은 입력값 입니다.", " input error", "INPUT_ERROR"),
    INPUT_USERID_ERROR(21004, "유효하지 않은 아이디 입니다.", "input userId error", "INPUT_USERID_ERROR"),
    INPUT_USEREMAIL_ERROR(21005, "유효하지 않은 이메일 입니다.", "input useremail error", "INPUT_USEREMAIL_ERROR"),
    INPUT_USERPASSWORD_ERROR(21006, "유효하지 않은 비밀번호 입니다.", "input userpassword error", "INPUT_USERPASSWORD_ERROR"),
    NOT_INPUT_FILE(21007, "파일을 등록해주세요.", " not input file", "NOT_INPUT_FILE"),
    REGIST_USEREINTO_ERROR(21008, "사용자 정보 등록 에러", "regist userinfo, error", "REGIST_USEREINTO_ERROR"),


    // 22000 ~  auth, role
    NOT_AUTH(22001, "Not Auth", "This account is not Auth. Please contact the manager.", "NOT_AUTH"),
    NOT_ADMIN(22002, "Not Admin", "This account is not admin. Please contact the manager.", "NOT_ADMIN"),
    NOT_PERMISSION(22003, "Has not Permission", "This account not has role. Please contact the manager.", "NOT_PERMISSION"),
    NOT_EXIST_USER(22004, "User not exist", "User does not exist. Please check again.", "NOT_EXIST_USER"),
    NOT_INVALID_TOKEN(22005, "유효하지않은 토큰입니다.", "Invalid token", "NOT_INVALID_TOKEN"),

    // 25001 DB Error,  Database, SQL
    SQL_GENERAL_WARNING(10, "SQL OK But not success", "No data, No Update, etc", "SQL_GENERAL_WARNING"),
    SQL_EXCEPTION(25001, "SQL error", "SQLException. Please contact the manager.", "SQL_EXCEPTION"),


    // 26000~ App Error
    IO_EXCEPTION(21009, "IO exception", " IOException. Please contact the manager.", "IO_EXCEPTION"),
    SERVER_INTERNAL_EXCEPTION(26001, "Server error", "Server Internal Error.", "SERVER_INTERNAL_EXCEPTION"),
    WRONG_LANGUAGE_SET(26002, "language set data is not set!", "Server Internal Error.", "WRONG_LANGUAGE_SET"),


    // 90001~ default, unknown
    UNKNOWN(90001, "Server error", "Sever Internal Error.", "UNKNOWN");

    private final int code;
    private final String message;
    private final String detailMessage;
    private final String messageKey;

    ErrorType(int code, String message, String detailMessage, String messageKey) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
        this.messageKey = messageKey;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
