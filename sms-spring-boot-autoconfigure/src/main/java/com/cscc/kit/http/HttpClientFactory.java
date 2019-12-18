package com.cscc.kit.http;

import com.cscc.kit.http.clients.CompatibleUrlConnClient;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;

/**
 * @author ajoe.Liu
 */
@SuppressWarnings("unchecked")
public class HttpClientFactory {

    public static String HTTP_CLIENT_IMPL_KEY = "sdk.httpclient";
    public static String COMPATIBLE_HTTP_CLIENT_CLASS_NAME = CompatibleUrlConnClient.class.getName();

    public static AbstractHttpClient buildClient(HttpClientConfig clientConfig) {
        try {
            if (clientConfig == null) {
                clientConfig = HttpClientConfig.getDefault();
            }

            String customClientClassName = null;
            if (clientConfig.isCompatibleMode()) {
                customClientClassName = COMPATIBLE_HTTP_CLIENT_CLASS_NAME;
            } else if (clientConfig.getClientType() == HttpClientType.Custom && (clientConfig.getCustomClientClassName()!=null&&!clientConfig.getCustomClientClassName().isEmpty())) {
                customClientClassName = clientConfig.getCustomClientClassName();
            } else {
                customClientClassName = System.getProperty(HTTP_CLIENT_IMPL_KEY);
            }
            if (StringUtils.isEmpty(customClientClassName)) {
                customClientClassName = clientConfig.getClientType().getImplClass().getName();
            }
            Class httpClientClass = Class.forName(customClientClassName);
            if (!AbstractHttpClient.class.isAssignableFrom(httpClientClass)) {
                throw new IllegalStateException(String.format("%s is not assignable from http.IHttpClient", customClientClassName));
            }
            Constructor<? extends AbstractHttpClient> constructor = httpClientClass.getConstructor(HttpClientConfig.class);
            return constructor.newInstance(clientConfig);
        } catch (Exception e) {
            // keep compatibility
            throw new IllegalStateException("HttpClientFactory buildClient failed", e);
        }
    }

}
