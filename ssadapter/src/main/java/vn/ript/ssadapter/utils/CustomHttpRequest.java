package vn.ript.ssadapter.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;

public class CustomHttpRequest {

    String method;
    String url;
    Map<String, String> headers;

    public CustomHttpRequest(String method, String url) {
        this.method = method;
        this.url = url;
        this.headers = new HashMap<>();
    }
    
    public CustomHttpRequest(String method, String url, Map<String, String> headers) {
        this.method = method;
        this.url = url;
        this.headers = headers;
    }

    public HttpResponse request() {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        try {
            sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContextBuilder.build(), new NoopHostnameVerifier());
            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();
            
            HttpRequestBase httpRequest = null;
            if (this.method.equalsIgnoreCase("Get")) {
                httpRequest = new HttpGet(url);
            } else if (this.method.equalsIgnoreCase("Post")) {
                httpRequest = new HttpPost(url);
            } else if (this.method.equalsIgnoreCase("Put")) {
                httpRequest = new HttpPut(url);
            } else if (this.method.equalsIgnoreCase("Patch")) {
                httpRequest = new HttpPatch(url);
            } else if (this.method.equalsIgnoreCase("Delete")) {
                httpRequest = new HttpDelete(url);
            } else if (this.method.equalsIgnoreCase("Head")) {
                httpRequest = new HttpHead(url);
            } else if (this.method.equalsIgnoreCase("Options")) {
                httpRequest = new HttpOptions(url);
            } else if (this.method.equalsIgnoreCase("Trace")) {
                httpRequest = new HttpTrace(url);
            } else {
                httpRequest = new HttpGet(url);
            }

            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                System.out.println(entry.getKey() + "/" + entry.getValue());
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }

            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpRequest);
            return httpResponse;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResponse request(HttpEntity entity) {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        try {
            sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContextBuilder.build(), new NoopHostnameVerifier());
            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();
            
            HttpEntityEnclosingRequestBase httpRequest = null;
            if (this.method.equalsIgnoreCase("Post")) {
                httpRequest = new HttpPost(url);
            } else if (this.method.equalsIgnoreCase("Put")) {
                httpRequest = new HttpPut(url);
            } else if (this.method.equalsIgnoreCase("Patch")) {
                httpRequest = new HttpPatch(url);
            } else {
                httpRequest = new HttpPost(url);
            }

            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                System.out.println(entry.getKey() + "/" + entry.getValue());
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }

            httpRequest.setEntity(entity);

            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpRequest);
            return httpResponse;
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
