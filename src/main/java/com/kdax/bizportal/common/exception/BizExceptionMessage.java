package com.kdax.bizportal.common.exception;


import java.util.Locale;

public class BizExceptionMessage extends RuntimeException {
    private int code;
    private ErrorType errorType;
    private String msgTypCod; //Q : Question, C : Critical, I : Information, E : Exclamation , B : system
    private String msgCaption;
    private Locale msgLocale;

    public BizExceptionMessage(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.code = errorType.getCode();
        this.msgTypCod = "";
        this.msgCaption = "";
        this.msgLocale = Locale.KOREA;
    }

    public BizExceptionMessage(ErrorType errorType, Locale localeInfo) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.code = errorType.getCode();
        this.msgTypCod = "";
        this.msgCaption = "";
        this.msgLocale = localeInfo;
    }

    public BizExceptionMessage(ErrorType errorType, String subMessage) {
        super(errorType.getMessage() + " (" + subMessage + ")");
        this.errorType = errorType;
        this.code = errorType.getCode();
        this.msgTypCod = "";
        this.msgCaption = "";
        this.msgLocale = Locale.KOREA;
    }

    public BizExceptionMessage(String msgTypCod, ErrorType errorType, String subMessage) {
        super(errorType.getMessage() + " (" + subMessage + ")");
        this.errorType = errorType;
        this.code = errorType.getCode();
        this.msgTypCod = msgTypCod;
        this.msgCaption = "";
        this.msgLocale = Locale.KOREA;
    }

    public BizExceptionMessage(String msgTypCod, ErrorType errorType, String subMessage, String msgCaption) {
        super(errorType.getMessage() + " (" + subMessage + ")");
        this.errorType = errorType;
        this.code = errorType.getCode();
        this.msgTypCod = msgTypCod;
        this.msgCaption = msgCaption;
        this.msgLocale = Locale.KOREA;
    }

    public int getCode() {
        return code;
    }
    public String getMsgTypCod() {
        return msgTypCod;
    }
    public String getMsgCaption() {
        return msgCaption;
    }
    public ErrorType getErrorType() {
        return this.errorType;
    }
    public Locale getMsgLocale() { return msgLocale; }
}
