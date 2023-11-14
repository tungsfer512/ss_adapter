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

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.pattern.Util;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> login) {
        
        String url = "https://" + Utils.ipSS + ":4000/login";

        CustomHttpRequest customHttpRequest = new CustomHttpRequest("POST", url);

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addTextBody("username", login.get("username"));
        multipartEntityBuilder.addTextBody("password", login.get("password"));
        HttpEntity multipartHttpEntity = multipartEntityBuilder.build();

        HttpResponse httpResponse = customHttpRequest.request(multipartHttpEntity);

        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            System.out.println("Form data uploaded successfully!");
        } else {
            System.out.println("Error uploading form data: " + httpResponse.getStatusLine());
        }
        CustomResponse<Map<String, Object>> customResponse = new CustomResponse<>(httpResponse.getStatusLine().getStatusCode());

        Map<String, Object> resData = new HashMap<String, Object>();
        Map<String, Object> resUser = new HashMap<String, Object>();
        resData.put("accessToken", "abcxyz");
        resUser.put("username", login.get("username"));
        resUser.put("systemRole", "Admin");
        resData.put("user", resUser);

        return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), resData);
    }
}
