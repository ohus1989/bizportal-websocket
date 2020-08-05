package com.kdax.bizportal.common.exception;

public enum ErrorType {
    ALL_SUCCESS_OK(0, "OK.", "all thing sucess."),
    SQL_GENERAL_WARNING(10, "SQL OK But not success", "No data, No Update, etc"),

    DUPLICATION_USER_ERROR(10001, "중복된 사용자입니다.", "duplicate user.."),
    NOT_FOUND_USER_ERROR(10002, "유효하지 않은 사용자입니다.", "not found user in database"),
    INPUT_ERROR(10003, "유효하지 않은 입력값 입니다.", " input error"),
    INPUT_USERID_ERROR(10004, "유효하지 않은 아이디 입니다.", "input userId error"),
    INPUT_USEREMAIL_ERROR(10005, "유효하지 않은 이메일 입니다.", "input useremail error"),
    INPUT_USERPASSWORD_ERROR(10006, "유효하지 않은 비밀번호 입니다.", "input userpassword error"),
    NOT_INPUT_FILE(10007, "파일을 등록해주세요.", " not input file"),
    NOT_OS(10008, "Linux가 아닙니다", " not linux"),
    IO_EXCEPTION(10009, "IO exception", " IOException. Please contact the manager."),


    // 500200 Database, SQL
    SQL_EXCEPTION(500200, "SQL error", "SQLException. Please contact the manager."),



    // 500900 auth, role
    NOT_AUTH(500900, "Not Auth", "This account is not Auth. Please contact the manager."),
    NOT_ADMIN(500901, "Not Admin", "This account is not admin. Please contact the manager."),


    NOT_PERMISSION(500920, "Has not Permission", "This account not has role. Please contact the manager."),
    NOT_EXIST_USER(500940, "User not exist", "User does not exist. Please check again."),


    SERVER_INTERNAL_EXCEPTION(500500, "Server error", "Server Internal Error."),
    WRONG_LANGUAGE_SET(500600, "language set data is not set!", "Server Internal Error."),

    // 600100 ~ 600900 asis-error handler
    FROMASIS_MANAGE_ERROR(600100, "WARN", "Message From DataBase."),
    FROMASIS_OPDSAVE_ERROR(600101, "WARN", "OPD SAVE ALL ERROR"),
    FROMASIS_OCMDB_ERROR(600102, "WARN", "OCM Insert, Update"),
    FROMASIS_OCMCHECK1_ERROR(600103, "WARN", "OCMNUM is empty"),
    FROMASIS_ODRSAVE_ERROR(600104, "WARN", "ORDER save error"),
    FROMASIS_OPDINFOSAVE_ERROR(600101, "WARN", "OPD INFO SAVE ALL ERROR"),
    FROMASIS_NOTIMPLEMENT_ERROR(600901, "WARN", "We don't understand as-is function, not implemented."),
    FROMASIS_MESSAGEBOX_ERROR(600800, "WARN", ""),

    // default, unknown
    UNKNOWN(500, "Server error", "Sever Internal Error.");




    private final int code;
    private final String message;
    private final String detailMessage;

    ErrorType(int code, String message, String detailMessage) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
