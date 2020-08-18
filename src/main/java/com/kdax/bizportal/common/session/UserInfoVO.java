package com.kdax.bizportal.common.session;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
public class UserInfoVO {
    private String UidCode;
    private String UidAdpDatetime;
    private String UidName;
    private String UidEngName;
    private String UidPwd;
    private String UidEmpNume;
    private String UidLevel;
    private Locale UidLocale;

}
