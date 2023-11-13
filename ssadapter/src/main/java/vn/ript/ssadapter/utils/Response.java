package vn.ript.ssadapter.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    public static ResponseEntity<Map<String, Object>> Response_no_data (int status) {
        Map<String, Object> res = new HashMap<>();
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }
    public static ResponseEntity<Map<String, Object>> Response_data (int status, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }
    public static ResponseEntity<Map<String, Object>> Response_data (int status, String data) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }
}
