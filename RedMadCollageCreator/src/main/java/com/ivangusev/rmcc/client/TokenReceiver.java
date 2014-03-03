package com.ivangusev.rmcc.client;

/**
 * Created by ivan on 21.02.14.
 */
public class TokenReceiver {

    private static TokenReceiver sInstance;

    public static TokenReceiver getInstance() {
        if(sInstance == null) sInstance = new TokenReceiver();
        return sInstance;
    }

    private TokenReceiver() {
    }
}
