package com.kdax.bizportal.common.constants;

public class GlobalConstants {
    public static final String JWT_USER_KEY = "bizportal_auth";
    public static final String BIZPORTAL_CHARSET="UTF-8";
    public static final String DEFAULT_CHARSET="UTF-8";
    public static final String TOKEN_PREFIX="BIZPORTAL_";
    public static final String TOKEN_HEADER="bizportal-access-token";
    public static final String TOKEN_JWTID="BIZPORTAL";

    public static final String DEFALUT_MENUVERSION ="001";
    public static final String DEFALUT_SALT_KEY ="KKK";


    public static final int DEFALUT_REDIS_TIMEOUT_USERINFO = 3600; //second

    /* esignon API*/
    public static final String ESIGNON_COMPANY_ID = "TESTAPI";
    public static final String ESIGNON_TOKEN_HEADER = "ESIGNON-access-token";
    public static final String ESIGNON_STATUS = "ESIGNON-access-status";
    public static final String ESIGNON_STATUS_SUCCESS = "SUCCESS";
    public static final String ESIGNON_STATUS_FAIL = "FAIL";
    /* esignon API URL*/
    public static final String ESIGNON_DOMAIN = "https://docs.esignon.net";
    public static final String ESIGNON_GET_TOKEN_API =ESIGNON_DOMAIN+"/api/{companyId}/login";
    /* esignon request code*/
    public static final String ESIGNON_GET_TOKEN_REQ ="1001Q";

}
