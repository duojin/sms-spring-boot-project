package com.cscc.kit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.*;

/**
 * Http请求工具类
 * @author ajoe.Liu
 */
@SuppressWarnings("unchecked")
public final class MiniHttpClientUtils {


    private static final int connectionTimeoutMillis = 15000;
    private static final int readTimeoutMillis = 15000;
    private static final int writeTimeoutMillis = 15000;


    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    private static final String CHARSET = "UTF-8";
    private static final String GET  = "GET";
    private static final String POST = "POST";

    private MiniHttpClientUtils() {}

    public static String get(String url) {
        return get(url,null,null);
    }

    public static String get(String url, Map<String, String> queryParas) {
        return get(url, queryParas,null);
    }

    public static String get(String url, Map<String, String> queryParas ,Map<String, String> headers) {
        return delegate.get(url, queryParas, headers);
    }

    public static String post(String url, String data) {
        return post(url, data,null);
    }
    public static String post(String url, Map<String, String> formUrlencodedParas) {
        String data = buildFormEncodedQureyString(formUrlencodedParas,true);
        return delegate.post(url, data, null);
    }
    public static String post(String url, String data, Map<String, String> headers) {
        return delegate.post(url, data, headers);
    }

    public static String postUseCert(String url, String data, String certPath, String certPass) {
        return postUseCert(url, data, certPath, certPass,null);
    }

    public static String postUseCert(String url, String data, String certPath, String certPass,Map<String, String> headers) {
        return delegate.postUseCert(url, data, certPath, certPass, headers);
    }
    /**
     * http请求工具 委托
     * 优先使用OkHttp
     * 最后使用JdkHttp
     */
    private interface HttpDelegate {
        /**
         * get请求
         * @param url
         * @param queryParas
         * @param headers
         * @return
         */
        String get(String url, Map<String, String> queryParas, Map<String, String> headers);

        /**
         * post 请求
         * @param url
         * @param data
         * @param headers
         * @return
         */
        String post(String url, String data, Map<String, String> headers);

        /**
         * 附带证书的请求
         * @param url
         * @param data
         * @param certPath
         * @param certPass
         * @param headers
         * @return
         */
        String postUseCert(String url, String data, String certPath, String certPass, Map<String, String> headers);
    }

    /**
     * http请求工具代理对象
     */
    private static final HttpDelegate delegate;

    static {
        HttpDelegate delegateToUse = null;
        // okhttp3.OkHttpClient?
        if (ClassUtils.isPresent("okhttp3.OkHttpClient", MiniHttpClientUtils.class.getClassLoader())) {
            delegateToUse = new OkHttp3Delegate();
        } else {
            delegateToUse = new JdkHttpDelegate();
        }
        delegate = delegateToUse;
    }

    /**
     * OkHttp3代理
     */
    private static class OkHttp3Delegate implements HttpDelegate {
        private static final okhttp3.MediaType MEDIA_TYPE_FORM =
                okhttp3.MediaType.parse("application/x-www-form-urlencoded");
        private static final okhttp3.MediaType MEDIA_TYPE_STREAM =
                okhttp3.MediaType.parse("application/octet-stream");

        private okhttp3.OkHttpClient httpClient;
        public OkHttp3Delegate() {
            // 分别设置Http的连接,写入,读取的超时时间
            httpClient = new okhttp3.OkHttpClient().newBuilder()
                    .connectTimeout(connectionTimeoutMillis, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeoutMillis, TimeUnit.MILLISECONDS)
                    .build();
        }
        private static class TrustAnyTrustManager implements X509TrustManager {
    		@Override
    		public X509Certificate[] getAcceptedIssuers() {
    			return null;  
    		}
    		@Override
    		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    		}
    		@Override
    		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    		}
    	}

        private String exec(okhttp3.Request request) {
            try {
                okhttp3.Response response = httpClient.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Unexpected code " + response);
                }
                return response.body().string();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String get(String url, Map<String, String> queryParas, Map<String, String> headersParas) {
            okhttp3.Headers headers = buildHeaders(headersParas);
            okhttp3.HttpUrl httpUrl = buildUrlWithParas(url,queryParas);
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(httpUrl)
                    .get()
                    .headers(headers)
                    .build();
            return exec(request);
        }

        @Override
        public String post(String url, String data, Map<String, String> headersParas) {
            okhttp3.RequestBody body = buildPostBody(data);
            okhttp3.Headers headers = buildHeaders(headersParas);
            okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url).headers(headers)
                .post(body)
                .build();
            return exec(request);
        }

        private okhttp3.HttpUrl buildUrlWithParas(String url, Map<String, String> queryParas){
            okhttp3.HttpUrl.Builder urlBuilder = okhttp3.HttpUrl.parse(url).newBuilder();
            if(queryParas == null) {
                queryParas = Collections.EMPTY_MAP;
            }
            for (Entry<String, String> entry : queryParas.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            return urlBuilder.build();
        }
        private okhttp3.RequestBody buildPostBody(String data){
            return okhttp3.RequestBody.create(data, MEDIA_TYPE_FORM);
        }

        private okhttp3.Headers buildHeaders(Map<String, String> headersParas){
            if(headersParas == null) {
                headersParas = Collections.EMPTY_MAP;
            }
            return okhttp3.Headers.of(headersParas);
        }

        @Override
        public String postUseCert(String url, String data, String certPath, String certPass, Map<String, String> headersParas) {
            okhttp3.RequestBody body = buildPostBody(data);
            okhttp3.Headers headers = buildHeaders(headersParas);
            okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url).headers(headers)
                .post(body)
                .build();

            InputStream inputStream = null;
            try {
                KeyStore clientStore = KeyStore.getInstance("PKCS12");
                inputStream = new FileInputStream(certPath);
                char[] passArray = certPass.toCharArray();
                clientStore.load(inputStream, passArray);

                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(clientStore, passArray);
                KeyManager[] kms = kmf.getKeyManagers();
                SSLContext sslContext = SSLContext.getInstance("TLSv1");

                sslContext.init(kms, null, new SecureRandom());

                okhttp3.OkHttpClient httpsClient = new okhttp3.OkHttpClient()
                        .newBuilder()
                        .connectTimeout(connectionTimeoutMillis, TimeUnit.MILLISECONDS)
                        .readTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS)
                        .writeTimeout(writeTimeoutMillis, TimeUnit.MILLISECONDS)
                        .sslSocketFactory(sslContext.getSocketFactory(), new TrustAnyTrustManager())
                        .build();

                okhttp3.Response response = httpsClient.newCall(request).execute();

                if (!response.isSuccessful()) {
                    throw new RuntimeException("Unexpected code " + response);
                }

                return response.body().string();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (inputStream != null) {
                    	inputStream.close();
                    }
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }

        public InputStream download(String url, String data) throws IOException {
            okhttp3.Request request;
            if (null!=data && !data.isEmpty()) {
                okhttp3.RequestBody body = buildPostBody(data);
                request = new okhttp3.Request.Builder().url(url).post(body).build();
            } else {
                request = new okhttp3.Request.Builder().url(url).get().build();
            }
            okhttp3.Response response = httpClient.newCall(request).execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().byteStream();
        }

        public String upload(String url, File file, String params) {
            okhttp3.RequestBody fileBody = okhttp3.RequestBody.create(file,MEDIA_TYPE_STREAM);
            okhttp3.MultipartBody.Builder builder = new okhttp3.MultipartBody.Builder()
                    .setType(okhttp3.MultipartBody.FORM)
                    .addFormDataPart("media", file.getName(), fileBody);
            if (null!=params && !params.isEmpty()) {
                builder.addFormDataPart("description", params);
            }
            okhttp3.RequestBody requestBody = builder.build();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            return exec(request);
        }
    }
    

    /**
     * JdkHttpDelegate代理
     */
    private static class JdkHttpDelegate implements HttpDelegate {
        /**
         * https 域名校验
         * @author liuxj
         */
        private static class TrustAnyHostnameVerifier implements HostnameVerifier {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }
        /**
         * https 证书管理
         * @author liuxj
         */
        private static class TrustAnyTrustManager implements X509TrustManager {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }
        private static final SSLSocketFactory SSL_SOCKET_FACTORY = initSSLSocketFactory();
        private static final TrustAnyHostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
        private static SSLSocketFactory initSSLSocketFactory() {
            try {
                TrustManager[] tm = {new TrustAnyTrustManager() };
                // ("TLS", "SunJSSE");
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tm, new java.security.SecureRandom());
                return sslContext.getSocketFactory();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        @Override
        public String get(String url, Map<String, String> queryParas, Map<String, String> headers) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), GET, headers);
                conn.connect();
                return readResponseString(conn);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        public String post(String url, String data, Map<String, String> headers) {
            HttpURLConnection conn = null;
            try {
                conn = getHttpConnection(url, POST, headers);
                conn.connect();

                if (data != null) {
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes(CHARSET));
                    out.flush();
                    out.close();
                }

                return readResponseString(conn);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        public String postUseCert(String url, String data, String certPath, String certPass, Map<String, String> headers) {
        	HttpsURLConnection conn = null;
            OutputStream out = null;
            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                KeyStore clientStore = KeyStore.getInstance("PKCS12");
                clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(clientStore, certPass.toCharArray());
                KeyManager[] kms = kmf.getKeyManagers();
                SSLContext sslContext = SSLContext.getInstance("TLSv1");

                sslContext.init(kms, null, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                URL _url = new URL(url);
                conn = (HttpsURLConnection) _url.openConnection();

                conn.setConnectTimeout(connectionTimeoutMillis);
                conn.setReadTimeout(readTimeoutMillis);
                conn.setRequestMethod(POST);
                conn.setDoOutput(true);
                conn.setDoInput(true);

                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
                conn.connect();

                out = conn.getOutputStream();
                out.write(data.getBytes(CHARSET));
                out.flush();

                return readResponseString(conn);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (out != null) {
                    	out.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
            URL urlObject = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)urlObject.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection)conn).setSSLSocketFactory(SSL_SOCKET_FACTORY);
                ((HttpsURLConnection)conn).setHostnameVerifier(TRUST_ANY_HOSTNAME_VERIFIER);
            }

            conn.setRequestMethod(method);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setConnectTimeout(connectionTimeoutMillis);
            conn.setReadTimeout(readTimeoutMillis);

            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            return conn;
        }
        private static String readResponseString(HttpURLConnection conn) {
            BufferedReader reader = null;
            try {
                StringBuilder ret;
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));
                String line = reader.readLine();
                if (line != null) {
                    ret = new StringBuilder();
                    ret.append(line);
                } else {
                    return "";
                }

                while ((line = reader.readLine()) != null) {
                    ret.append('\n').append(line);
                }
                return ret.toString();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private static String buildUrlWithQueryString(String url, Map<String, String> queryParas){
        if(null==url || url.isEmpty()){
            throw new IllegalStateException("url can not empty");
        }
        StringBuilder result = new StringBuilder(url);
        if(url.contains("?")){
            result.append("&");
        }else{
            result.append("?");
        }
        result.append(buildFormEncodedQureyString(queryParas,false));
        return result.toString();
    }
    /**
	 * 构建FormEncoded字符串
	 * make by liuxj 2018年1月31日上午8:55:02
	 * @param queryParas
	 * @param canEmpty   参数的值是否允许空值
	 * @return
	 */
	public static String buildFormEncodedQureyString(Map<String, String> queryParas,boolean canEmpty) {
		if (queryParas == null || queryParas.isEmpty()) {
			return "";
		}
        StringBuilder result = new StringBuilder();
		boolean isFirst = true ;
		for (Entry<String, String> entry : queryParas.entrySet()) {
			if (isFirst) {
				isFirst = false;
			} else {
                result.append('&');
			}
			String key = entry.getKey();
			String value = entry.getValue();
			value = value==null?"":value;
			if(value.isEmpty() && !canEmpty) {
				continue;
			}
            try {
                value = URLEncoder.encode(value, CHARSET);
            } catch (UnsupportedEncodingException e) {}
            result.append(key).append('=').append(value);
		}
		return result.toString();
	}
}
