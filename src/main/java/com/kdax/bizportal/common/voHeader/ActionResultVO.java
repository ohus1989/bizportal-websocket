package com.kdax.bizportal.common.voHeader;


public class ActionResultVO extends ResponseHeaderVO{
    private Object resultVO;

    public ActionResultVO(Object objVO) {
        //super(errorType);
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

}
