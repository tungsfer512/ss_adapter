package vn.ript.ssadapter.controller;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.List;
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
import org.hibernate.mapping.Array;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ArrayListMultimap;

import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.CustomResponseData;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> login(@RequestHeader Map<String, String> headers) {
        System.out.println(headers);
        Map<String, Object> resData = new HashMap<String, Object>();
        resData.put("_id", 1);
        resData.put("username", "xrd");
        resData.put("systemRole", "admin");
        resData.put("hoDem", "Bui Van");
        resData.put("ten", "Tung");
        resData.put("gioiTinh", "nam");
        resData.put("email", "tungbv5122001@gmail.com");
        return CustomResponse.Response_data(200, resData);
    }

    @RequestMapping(path = "/members", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> members() {
        try {
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContextBuilder.build(), new NoopHostnameVerifier());

            // Create a custom HTTP client that trusts all certificates
            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();

            HttpGet httpGet = new HttpGet("https://" + Utils.ipSS + "/listClients");

            // Set header accept json
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");

            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get members successfully!");
            } else {
                System.out.println("Get member failed: " + httpResponse.getStatusLine());
            }
            Map<String, Object> resData = new HashMap<String, Object>();
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(jsonResponse);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            List<Object> members = jsonObject.getJSONArray("member").toList();

            return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), members);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(path = "/services", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> services(
            // @PathVariable String subsystemId,
            @RequestHeader(value = "Subsystem-Code") String subsystemCode,
            // @RequestHeader(value = "Content-Type") String contentType,
            @RequestHeader(value = "X-Road-Client") String xRoadClient) {
        try {

            System.out.println(subsystemCode);
            // System.out.println(contentType);
            System.out.println(xRoadClient);
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContextBuilder.build(), new NoopHostnameVerifier());

            // Create a custom HTTP client that trusts all certificates
            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();

            String subsystem_code = subsystemCode.replace(':', '/');
            HttpGet httpGet = new HttpGet("https://" + Utils.ipSS + "/r1/" + subsystem_code + "/listMethods");

            // Set header accept json
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("X-Road-Client", xRoadClient);

            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get service successfully!");
            } else {
                System.out.println("Get service failed: " + httpResponse.getStatusLine());
            }
            Map<String, Object> resData = new HashMap<String, Object>();
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(jsonResponse);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            List<Object> services = jsonObject.getJSONArray("service").toList();

            return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), services);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(path = "/getEndpoints", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getEndpoints(
            // @PathVariable String subsystemId,
            @RequestHeader(value = "Subsystem-Code") String subsystemCode,
            @RequestHeader(value = "Service-Code") String serviceCode,
            @RequestHeader(value = "Service-Endpoint") String serviceEndpoint,
            @RequestHeader(value = "X-Road-Client") String xRoadClient) {
        try {

            System.out.println(subsystemCode);
            // System.out.println(contentType);
            System.out.println(xRoadClient);
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContextBuilder.build(), new NoopHostnameVerifier());

            // Create a custom HTTP client that trusts all certificates
            HttpClient httpClient = HttpClientBuilder.create()
                    .setSSLSocketFactory(sslConnectionSocketFactory)
                    .build();

            String subsystem_code = subsystemCode.replace(':', '/');
            HttpGet httpGet = new HttpGet("https://" + Utils.ipSS + "/r1/" + subsystem_code + "/" + serviceCode + "/" + serviceEndpoint);

            // Set header accept json
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("X-Road-Client", xRoadClient);

            HttpResponse httpResponse;
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get endpoint successfully!");
            } else {
                System.out.println("Get endpoint failed: " + httpResponse.getStatusLine());
            }
            Map<String, Object> resData = new HashMap<String, Object>();
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            System.out.println(jsonResponse);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            Map<String, Object> endpoints = jsonObject.toMap();

            return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), endpoints);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
