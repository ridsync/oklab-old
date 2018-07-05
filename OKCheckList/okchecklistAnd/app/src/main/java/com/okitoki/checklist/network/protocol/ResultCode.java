package com.okitoki.checklist.network.protocol;

/**
 * @author okc
 * @version 1.0
 * @see
 * @since 2015-12-20.
 *
 *  protocol -
 */
public class ResultCode {

    public static final int HTTP_SUCCESS_OK = 200;
    public static final int SERVER_ERROR = 500;
    public static final int SESSION_ERROR = 300;
    public static final int CLIENT_ERROR = 400;
    public static final int JSON_PRASE_ERROR = 100;
    public static final int CANCELLED = -100;
    public static final int CONNECT_FAIL = -200;

    public static final int API_NO_ERROR = HTTP_SUCCESS_OK;
    public static final int API_SUCCESS = 1;
    public static final int API_ERRORSSS = 2;

}
