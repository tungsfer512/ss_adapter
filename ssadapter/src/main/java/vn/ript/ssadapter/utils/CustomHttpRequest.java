package vn.ript.ssadapter.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

class SkipSSLHttpClient {
    public static CloseableHttpClient Create() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                        String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                        String authType) {
                }
            } };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setSSLContext(sc).build();
            return httpClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

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
        try {
            CloseableHttpClient httpClient = SkipSSLHttpClient.Create();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResponse request(HttpEntity entity) {
        try {
            CloseableHttpClient httpClient = SkipSSLHttpClient.Create();

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
