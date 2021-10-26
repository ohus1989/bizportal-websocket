package com.kdax.bizportal.common.session;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Locale;

@Getter
@Setter
public class AuthTokenVO {
    private String uuid;
    private Date expireDate;
    private String userCodeId;
    private String userDeptId;
    private String userLevel;
    private Boolean isOtp;
    private Locale userLocale;
}
