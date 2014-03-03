package com.ivangusev.rmcc.client.exception;

/**
 * Created by ivan on 21.02.14.
 */
public class HttpException extends Exception {

    private final int statusCode;
    private final String reasonPhrase;

    public HttpException(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
