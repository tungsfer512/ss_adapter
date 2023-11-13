package vn.ript.ssadapter.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    public static ResponseEntity<Map<String, Object>> Response_no_data (int status) {
        Map<String, Object> res = new HashMap<>();
        res.put("ErrorDesc", "InvalidArgument");
        res.put("ErrorCode", "-1");
        res.put("status", "FAIL");
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }
    public static ResponseEntity<Map<String, Object>> Response_data (int status, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        if (status == 500 || status == 400 || status == 404) {
            res.put("ErrorDesc", "InvalidArgument");
            res.put("ErrorCode", "-1");
            res.put("status", "FAIL");
        } else {
            res.put("ErrorDesc", "Thanh cong");
            res.put("ErrorCode", "0");
            res.put("status", "OK");
        }
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }
    public static ResponseEntity<Map<String, Object>> Response_data (int status, String data) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        if (status == 500 || status == 400 || status == 404) {
            res.put("ErrorDesc", "InvalidArgument");
            res.put("ErrorCode", "-1");
            res.put("status", "FAIL");
        } else {
            res.put("ErrorDesc", "Thanh cong");
            res.put("ErrorCode", "0");
            res.put("status", "OK");
        }
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }
}
