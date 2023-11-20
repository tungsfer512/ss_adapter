package vn.ript.ssadapter.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> login) {
        
        String url = "https://" + Utils.SS_IP + ":4000/login";

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

        Map<String, Object> resData = new HashMap<String, Object>();
        Map<String, Object> resUser = new HashMap<String, Object>();
        resData.put("accessToken", "abcxyz");
        resUser.put("username", login.get("username"));
        resUser.put("systemRole", "Admin");
        resData.put("user", resUser);

        return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), resData);
    }
}
