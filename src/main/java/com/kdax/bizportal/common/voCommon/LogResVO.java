package com.kdax.bizportal.common.voCommon;

import lombok.Data;

import java.util.Map;

@Data
public class LogResVO {
    private String txid;
    private int status;
    private Map headers;
    private Object body;
    private String contentType;
    private long responseTime;
}
