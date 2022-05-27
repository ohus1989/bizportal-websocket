package com.kdax.bizportal.common.voCommon;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ComCodeVO {
    private String codeTypeId;
    private String code;
    private String codeKname;
    private String codeEname;
    private String useYn;
    private Date createTime;
    private String createId;
    private Date updateTime;
    private String updateId;
    private String sortOrder;
    private String value;
    private String description;
    private String visibleYn;
    private String jsonYn;
    private String webhookUrl;

}
