package com.kdax.bizportal.common.voHeader;


import com.kdax.bizportal.common.exception.ErrorType;

public class ActionResultVO extends ResponseHeaderVO{
    private Object resultVO;

    public ActionResultVO(Object objVO) {
        //super(errorType);
        this.resultVO = objVO;
    }


    public ActionResultVO(Object objVO, int code) {
        //super(errorType);
        this.setCode(code);
        this.resultVO = objVO;
    }

    public ActionResultVO(Object objVO, ErrorType errorType) {
        //super(errorType);
        this.setCode(errorType.getCode());
        this.setMessage(errorType.getMessage());
        this.resultVO = objVO;
    }


    public ActionResultVO() {
        //super(errorType);
        setCode(0);
    }

    public Object getResultVO() {
        return resultVO;
    }
    public void setResultVO(Object resultVO) {
        this.resultVO = resultVO;
    }

    public void setErrorType(ErrorType errorType) {
        this.setCode(errorType.getCode());
        this.setMessage(errorType.getMessage());
    }

}
