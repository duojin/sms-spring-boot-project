package com.cscc.kit.http;


import com.cscc.kit.http.clients.CompatibleUrlConnClient;

/**
 * @author VK.Gao
 * @date 2018/03/29
 */
public enum HttpClientType {

    Compatible(CompatibleUrlConnClient.class),
    ApacheHttpClient(com.cscc.kit.http.clients.ApacheHttpClient.class),
    OkHttp(null),
    Custom(null),
    ;

    private Class<? extends AbstractHttpClient> implClass;

    HttpClientType(Class<? extends AbstractHttpClient> implClass) {
        this.implClass = implClass;
    }

    public Class<? extends AbstractHttpClient> getImplClass() {
        return implClass;
    }
}
