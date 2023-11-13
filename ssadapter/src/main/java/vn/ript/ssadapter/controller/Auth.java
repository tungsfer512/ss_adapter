package vn.ript.ssadapter.controller;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.Response;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Auth {
    
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> login) {
        try {
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(), new NoopHostnameVerifier());

            // Create a custom HTTP client that trusts all certificates
            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addTextBody("username", login.get("username"));
            multipartEntityBuilder.addTextBody("password", login.get("password"));
            System.out.println(multipartEntityBuilder);
    
            HttpPost httpPost = new HttpPost("https://10.29.1.228:4000/login");
    
    
            HttpEntity multipartHttpEntity = multipartEntityBuilder.build();
            httpPost.setEntity(multipartHttpEntity);
    
            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Form data uploaded successfully!");
            } else {
                System.out.println("Error uploading form data: " + httpResponse.getStatusLine());
            }
            Map<String, Object> resData = new HashMap<String, Object>();
            Map<String, Object> resUser = new HashMap<String, Object>();
            resData.put("accessToken", httpResponse.getStatusLine().getStatusCode());
            resUser.put("username", login.get("username"));
            resUser.put("systemRole", "Admin");
            resData.put("user", resUser);
            return Response.Response_data(httpResponse.getStatusLine().getStatusCode(), resData);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
