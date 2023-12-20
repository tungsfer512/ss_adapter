package vn.ript.ssadapter.controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomResponse;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @GetMapping("/users/me")
    public ResponseEntity<Map<String, Object>> login(@RequestHeader String token) {
        JSONObject resData = new JSONObject();
        resData.put("_id", 1);
        resData.put("username", "xrd");
        resData.put("systemRole", "admin");
        resData.put("hoDem", "Bui Van");
        resData.put("ten", "Tung");
        resData.put("gioiTinh", "nam");
        resData.put("email", "tungbv5122001@gmail.com");
        return CustomResponse.Response_data(200, resData.toString());
    }

}
