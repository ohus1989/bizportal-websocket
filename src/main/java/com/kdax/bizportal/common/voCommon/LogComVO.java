package com.kdax.bizportal.common.voCommon;

import com.kdax.bizportal.common.exception.BizExceptionMessage;
import com.kdax.bizportal.common.session.AuthTokenVO;
import lombok.Data;

@Data
public class LogComVO {
    LogReqVO request;
    LogResVO response;
    AuthTokenVO authTokenVO;
    StackTraceElement traceElement;
    BizExceptionMessage bizExceptionMessage;
}
