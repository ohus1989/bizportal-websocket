package com.kdax.bizportal.common.voCommon;

import lombok.Data;

import java.util.Map;

@Data
public class LogReqVO {
    private String txid;
    private String httpMethod;
    private String reqUri;
    private Map parameters;
    private Map headers;
    private Object body;
    private String className;
    private String classMethod;
    private String contentType;
}
