package com.simon.utils;

/**
 * Created by simon on 2017/2/28.
 */
public class ServerContext {
    public static final String MSG = "message";
    public static final String STATUS_CODE = "status";
    public static final String DATA = "data";
    public static final String USER_INFO = "userInfo";
    public static final String DEV_MSG = "developerMessage";
    public static final String MORE_INFO = "moreInfo";
    public static final String IP="120.25.152.172";

    public static final Integer SIGN_UP_STATUS = 1;
    public static final Integer SIGN_IN_STATUS = 2;
    public static final Integer SIGN_OUT_STATUS = 3;

    public static final String DAYU_URL_SANDBOX = "http://gw.api.tbsandbox.com/router/rest";
    public static final String DAYU_URL_REAL = "http://gw.api.taobao.com/router/rest";
    public static final String DAYU_APP_KEY = "23656595";
    public static final String DAYU_APP_SECRET = "f5b33f982cc1e5a8becbf37cd03e51fb";
    public static final String DAYU_SMS_FREE_SIGN_NAME = "益题库";
    public static final String DAYU_SMS_TEMPLATE_CODE = "SMS_50225027";

    public static final String JIGUANG_APP_KEY = "2cb1b6f5ee6c596abe813e49";
    public static final String JIGUANG_MASTER_SECRET = "aa81152ecac5776f2ff6db91";

    public static final Integer AUDIT_RESULT_WAIT = 0;
    public static final Integer AUDIT_RESULT_SUCCESS = 1;
    public static final Integer AUDIT_RESULT_REFUSED = 2;
    public static final Integer AUDIT_RESULT_RESUBMIT = 3;

    public static final String OAUTH_URI = "http://localhost:8182/oauth/token";
}
