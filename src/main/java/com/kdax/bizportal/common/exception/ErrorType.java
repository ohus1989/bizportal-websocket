package com.kdax.bizportal.common.exception;

public enum ErrorType {
    ALL_SUCCESS_OK(0, "OK.", "all thing sucess.", "ALL_SUCCESS_OK"),
    FAIL(1, "fail", "fail", "FAIL"),

    // 21000 ~  Sing In Message
    DUPLICATION_USER_ERROR(21001, "중복된 사용자입니다.", "duplicate user..", "DUPLICATION_USER_ERROR"),
    NOT_FOUND_USER_ERROR(21002, "유효하지 않은 사용자입니다.", "not found user in database", "NOT_FOUND_USER_ERROR"),
    INPUT_ERROR(21003, "유효하지 않은 입력값 입니다.", " input error", "INPUT_ERROR"),
    INPUT_USERID_ERROR(21004, "유효하지 않은 아이디 입니다.", "input userId error", "INPUT_USERID_ERROR"),
    INPUT_USEREMAIL_ERROR(21005, "유효하지 않은 이메일 입니다.", "input useremail error", "INPUT_USEREMAIL_ERROR"),
    INPUT_USERPASSWORD_ERROR(21006, "유효하지 않은 비밀번호 입니다.", "input userpassword error", "INPUT_USERPASSWORD_ERROR"),
    NOT_INPUT_FILE(21007, "파일을 등록해주세요.", " not input file", "NOT_INPUT_FILE"),
    REGIST_USEREINTO_ERROR(21008, "사용자 정보 등록 에러", "regist userinfo, error", "REGIST_USEREINTO_ERROR"),
    IO_EXCEPTION(21009, "IO exception", " IOException. Please contact the manager.", "IO_EXCEPTION"),
    INVALID_IDPASSWORD(21010, "존재하지 않는 아이디이거나, 잘못된 비밀번호입니다.", "Invalid id / password", "INVALID_IDPASSWORD"),
    NON_USER_RLOE(21011, "권한이 없습니다. 담당자에게 문의 바랍니다.", "NON_USER_RLOE", "NON_USER_RLOE"),
    NON_ACTIVE_USER(21012, "활성화 되지않은 사용자 입니다. 관리자에게 문의 바랍니다.", "NON_ACTIVE_USER", "NON_ACTIVE_USER"),
    CHANGE_PWD(21013, "비밀번호 사용기간이 지났습니다.", "비밀번호 사용기간(90일)이 지났습니다. 변경하시기 바랍니다.", "CHANGE_PWD"),
    OTP_CODE_NOT_VALID(21013, "OTP 코드가 맞지 않습니다. 다시 입력 해주세요.", "OTP_CODE_NOT_VALID", "OTP_CODE_NOT_VALID"),
    OTP_NON_REGISTRATION(21014, "OTP가 등록된 사용자가 아닙니다.", "OTP_NON_REGISTRATION", "OTP_NON_REGISTRATION"),
    OTP_CREATION_FAILED(21015, "OTP 생성 실패", "OTP_CREATION_FAILED", "OTP_CREATION_FAILED"),
    DUPLICATE_PASSWORD_FAILED(21016, "동일한 비밀번호를 사용할수 없습니다.", "You cannot use the same password.", "DUPLICATE_PASSWORD_FAILED"),
    PASSWORD_LENGTH_FAILED(21017, "8자리 이상 20자리 이하입니다.", "8 or more and 20 or less.", "PASSWORD_LENGTH_FAILED"),
    LOGIN_FAILED_SEVERAL_TIMES(21018, "로그인 5회 이상 실패하시어 10분간 로그인을 하실수 없습니다.", "You cannot log in for 10 minutes after failed login more than 5 times..", "LOGIN_FAILED_SEVERAL_TIMES"),


    // 22000 ~  auth, role
    NOT_AUTH(22001, "Not Auth", "This account is not Auth. Please contact the manager.", "NOT_AUTH"),
    NOT_ADMIN(22002, "Not Admin", "This account is not admin. Please contact the manager.", "NOT_ADMIN"),
    NOT_PERMISSION(22003, "Has not Permission", "This account not has role. Please contact the manager.", "NOT_PERMISSION"),
    NOT_EXIST_USER(22004, "User not exist", "User does not exist. Please check again.", "NOT_EXIST_USER"),
    NOT_INVALID_TOKEN(22005, "유효하지않은 토큰입니다.", "Invalid token", "NOT_INVALID_TOKEN"),
    NOT_OTP_REGISTRANT(22006, "OTP를 등록한 사용자가 아닙니다.", "You are not a registered user for otp", "NOT_OTP_REGISTRANT"),

    // 25001 DB Error,  Database, SQL
    SQL_GENERAL_WARNING(10, "SQL OK But not success", "No data, No Update, etc", "SQL_GENERAL_WARNING"),
    SQL_EXCEPTION(25001, "SQL error", "SQLException. Please contact the manager.", "SQL_EXCEPTION"),
    NO_DATA(25002, "조회된 데이터가 없습니다.", "조회된 데이터가 없습니다. 입력값을 확인 해주세요.", "SQL_RESULT_NODATA"),


    // 26000~ App Error
    SERVER_INTERNAL_EXCEPTION(26001, "Server error", "Server Internal Error.", "SERVER_INTERNAL_EXCEPTION"),
    WRONG_LANGUAGE_SET(26002, "language set data is not set!", "Server Internal Error.", "WRONG_LANGUAGE_SET"),

    // 27000~ external interface Error
    // ESIGNON interface
    ESIGNON_SERVER_EXCEPTION(27001, "esignon API 서버에 문제가 있습니다.", "esignon API 서버에 문제가 있습니다.", "ESIGNON_SERVER_EXCEPTION"),
    ESIGNON_HEADER_NOT_FOUND_EXCEPTION(27011, "esignon 응답에 헤더 값이 없습니다.", "esignon 응답에 http body 구성은 헤더, 바디가 포함된 map 형태입니다.", "ESIGNON_HEADER_NOT_FOUND_EXCEPTION"),
    ESIGNON_BODY_NOT_FOUND_EXCEPTION(27012, "esignon 응답에 바디 값이 없습니다.", "esignon 응답에 http body 구성은 헤더, 바디가 포함된 map 형태입니다.", "ESIGNON_BODY_NOT_FOUND_EXCEPTION"),
    ESIGNON_RESULT_FAIL_EXCEPTION(27013, "esignon 정상요청이 아닙니다.", "esignon 정상요청이 아닙니다.", "ESIGNON_RESULT_FAIL_EXCEPTION"),

    // 27100~ kakao api
    KAKAO_NON_TEMPLATE(27100, "등록된 템플릿이 아닙니다.", "등록된 템플릿이 아닙니다.", "KAKAO_NON_TEMPLATE"),
    KAKAO_NOT_COMPLATE(27101, "완료된 문서가 아닙니다.", "완료된 문서가 아닙니다.", "KAKAO_NOT_COMPLATE"),

    // 27200~ email
    EMAIL_NON_TEMPLATE(27200, "등록된 템플릿이 아닙니다.", "등록된 템플릿이 아닙니다.", "EMAIL_NON_TEMPLATE"),

    // 28000~ excel Error
    EXCEL_BANK_CODE_IS_NULL_EXCEPTION(28001, "Bank 코드가 없습니다.", "Bank 코드가 없습니다.", "EXCEL_BANK_CODE_IS_NULL_EXCEPTION"),
    EXCEL_BANK_CODE_IS_NOT_MATCH_EXCEPTION(28002, "excel 포멧이 설정된 Bank 코드가 아닙니다.", "excel 포멧이 설정된 Bank 코드가 아닙니다.", "EXCEL_BANK_CODE_IS_NOT_MATCH_EXCEPTION"),
    EXCEL_FORMAT_IS_EMPTY(28003, "excel 양식이 없습니다. json 양식을 등록하세요.", "excel 양식이 없습니다. json 양식을 등록하세요.", "EXCEL_FORMAT_IS_EMPTY"),
    DUPLICATE_BANK_ACCOUNT(28004, "중복된 은행코드가 존재합니다.", "중복된 은행코드가 존재합니다.", "DUPLICATE_BANK_ACCOUNT"),
    NOT_MATCH_BANK_ACCOUNT(28005, "해당 은행 코드와 매칭되는 데이터가 없습니다.", "해당 은행 코드와 매칭되는 데이터가 없습니다.", "NOT_MATCH_BANK_ACCOUNT"),
    EXCEL_CONVERT_OPTION_IS_NULL(28006, "excel 변경 옵션이 없습니다.", "excel 변경 옵션이 없습니다.", "EXCEL_CONVERT_OPTION_IS_NULL"),
    EXCEL_OPTION_IS_NULL(28007, "excel 옵션이 없습니다.", "excel 옵션이 없습니다.", "EXCEL_OPTION_IS_NULL"),
    EXCEL_CONVERT_EXCEPTION(28008, "excel 변환중 에러가 발생했습니다.", "excel 변환중 에러가 발생했습니다.", "EXCEL_CONVERT_EXCEPTION"),
    EXCEL_EMPTY_DB_TABLE_NAME(28009, "excel 변환중 DB 테이블이 없습니다.", "excel 변환중 DB 테이블이 없습니다.", "EXCEL_EMPTY_DB_TABLE_NAME"),
    EXCEL_EMPTY_ADD_VALUE_TYPE(28009, "excel 변환중 add value type 이 없습니다.", "excel 변환중 add value type 이 없습니다.", "EXCEL_EMPTY_ADD_VALUE_TYPE"),

    // 29000~ sales Error
    DEP_AMT_NOT_EXCCED_SALES_AMT(29000, "입금액은 매출액을 초과 할 수 없습니다.", "입금액은 매출액을 초과 할 수 없습니다.", "DEP_AMT_NOT_EXCCED_SALES_AMT"),

    // 30000~ leave Error
    LEAVE_NOT_CAPABLE(30000, "해당 기간에 존재하는 일정이 있습니다.", "해당 기간에 존재하는 일정이 있습니다.", "LEAVE_NOT_CAPABLE"),
    LEAVE_DUPLICATE(30001, "해당 기간에 중복되는 일정이 있습니다.", "해당 기간에 중복되는 일정이 있습니다.", "LEAVE_DUPLICATE"),
    LEAVE_EQUAL_TIME(30002, "시간대를 중복하여 등록하실수 없습니다.", "시간대를 중복하여 등록하실수 없습니다.", "LEAVE_EQUAL_TIME"),
    LEAVE_ADD_EXCEPTION(30003, "저장 실패.", "저장 실패.", "LEAVE_ADD_EXCEPTION"),

    // 90001~ default, unknown
    UNKNOWN(90001, "Server error", "Sever Internal Error.", "UNKNOWN"),
    ACCESSKEY_NULL(90002,"AccessKey is null","AccessKey is null","ACCESSKEY_NULL"),
    ACCESSKEY_NOT_EQUAL(90003,"AccessKey is not equal","AccessKey is not equal","ACCESSKEY_NOT_EQUAL"),
    // 91001~ default, unknown
    OBJECT_NULL_EXCEPTION(91001, "object is null", "object is null", "OBJECT_NULL_EXCEPTION");

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
