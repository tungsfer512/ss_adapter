package vn.ript.ssadapter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @RequestMapping(path = "/users/me", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> login(@RequestHeader String token) {
        System.out.println(token);
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
            String url = "https://" + Utils.SS_IP + "/listClients";
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("Content-type", "application/json");
            CustomHttpRequest customHttpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = customHttpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get members successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(jsonResponse);
    
                JSONObject jsonObject = new JSONObject(jsonResponse);
                List<Object> members = jsonObject.getJSONArray("member").toList();
    
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), members);
            } else {
                System.out.println("Get member failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CustomResponse.Response_data(500, "Loi Loi");
        }
    }

    @RequestMapping(path = "/services", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> services(
            // @PathVariable String subsystemId,
            @RequestHeader(value = "Subsystem-Code") String subsystemCode,
            // @RequestHeader(value = "Content-Type") String contentType,
            @RequestHeader(value = "X-Road-Client") String xRoadClient) {
        try {

            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("Content-type", "application/json");
            headers.put("X-Road-Client", xRoadClient);
            
            String subsystem_code = subsystemCode.replace(':', '/');
            String urlList = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/listMethods";
            String urlAllow = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/allowedMethods";
            
            CustomHttpRequest customHttpRequestList = new CustomHttpRequest("GET", urlList, headers);
            CustomHttpRequest customHttpRequestAllow = new CustomHttpRequest("GET", urlAllow, headers);

            HttpResponse httpResponseList = customHttpRequestList.request();
            HttpResponse httpResponseAllow = customHttpRequestAllow.request();

            if (httpResponseList.getStatusLine().getStatusCode() == 200) {
                // Map<String, Object> resData = new HashMap<String, Object>();
                String jsonResponseList = EntityUtils.toString(httpResponseList.getEntity());
                System.out.println(jsonResponseList);
                
                JSONObject jsonObjectList = new JSONObject(jsonResponseList);
                List<Object> servicesList = jsonObjectList.getJSONArray("service").toList();
                if (httpResponseAllow.getStatusLine().getStatusCode() == 200) {
                    String jsonResponseAllow = EntityUtils.toString(httpResponseAllow.getEntity());
                    System.out.println(jsonResponseAllow);
                    
                    JSONObject jsonObjectAllow = new JSONObject(jsonResponseAllow);
                    List<Object> servicesAllow = jsonObjectAllow.getJSONArray("service").toList();
                    for (Object obj : servicesAllow) {
                        System.out.println("=========================");
                        System.out.println(obj);
                        System.out.println("=========================");
                    }
                    System.out.println("Get endpoint successfully!");
                    return CustomResponse.Response_data(httpResponseAllow.getStatusLine().getStatusCode(), servicesList);
                } else {
                    System.out.println("Get allowed endpoint failed: " + httpResponseList.getStatusLine());
                    return CustomResponse.Response_data(500, httpResponseList.getStatusLine());
                }
            } else {
                System.out.println("Get endpoint failed: " + httpResponseList.getStatusLine());
                return CustomResponse.Response_data(500, httpResponseList.getStatusLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CustomResponse.Response_data(500, "Loi Loi");
        }
    }

    @RequestMapping(path = "/getEndpoints", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getEndpoints(
            // @PathVariable String subsystemId,
            @RequestHeader(value = "Subsystem-Code") String subsystemCode,
            @RequestHeader(value = "Service-Code") String serviceCode,
            @RequestHeader(value = "Service-Endpoint") String serviceEndpoint,
            @RequestHeader(value = "X-Road-Client") String xRoadClient) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("Content-type", "application/json");
            headers.put("X-Road-Client", xRoadClient);

            String subsystem_code = subsystemCode.replace(':', '/');
            String url = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/" + serviceCode + "/" + serviceEndpoint;
            
            CustomHttpRequest customHttpRequest = new CustomHttpRequest("GET", url, headers);

            HttpResponse httpResponse = customHttpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get endpoint successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(jsonResponse);
    
                JSONObject jsonObject = new JSONObject(jsonResponse);
                Map<String, Object> endpoints = jsonObject.toMap();
    
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), endpoints);
            } else {
                System.out.println("Get endpoint failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CustomResponse.Response_data(500, "Loi Loi");
        }
    }

}
