package com.kdax.bizportal.common.session;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Locale;

@Getter
@Setter
public class AuthTokenVO {
    private Date expireDate;
    private String uuid;
    private String userId;
    private String userLevel;
    private String userGroup;
    private String userDept;
    private Locale userLocale;
}
