package vn.ript.ssadapter.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class CustomResponse<T> {

    public interface RESPONSE_STATUS_CODE {
        Integer[] THANH_CONG = { 200, 201, 202, 203, 204, 205, 206 };
        Integer[] THAT_BAI = { 400, 401, 403, 404, 405, 406, 414, 415, 422, 500, 501, 502, 503, 504 };
    }

    public static List<Integer> SUCCESS_RESPONSE = Arrays.asList(RESPONSE_STATUS_CODE.THANH_CONG);
    public static List<Integer> FAIL_RESPONSE = Arrays.asList(RESPONSE_STATUS_CODE.THAT_BAI);

    Integer statusCode;
    CustomResponseData<T> responseData;

    public CustomResponse(int status) {
        this.statusCode = status;
    }

    public static ResponseEntity<Map<String, Object>> Response_no_data(int status) {
        Map<String, Object> res = new HashMap<>();
        if (SUCCESS_RESPONSE.contains(status)) {
            res.put("ErrorDesc", "Thanh cong");
            res.put("ErrorCode", "0");
            res.put("status", "OK");
        } else {
            res.put("ErrorDesc", "That bai");
            res.put("ErrorCode", "-1");
            res.put("status", "FAILED");
        }
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }

    public static ResponseEntity<Map<String, Object>> Response_data(int status, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        if (SUCCESS_RESPONSE.contains(status)) {
            res.put("ErrorDesc", "Thanh cong");
            res.put("ErrorCode", "0");
            res.put("status", "OK");
        } else {
            res.put("ErrorDesc", "That bai");
            res.put("ErrorCode", "-1");
            res.put("status", "FAILED");
        }
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }

    public static ResponseEntity<Map<String, Object>> Response_data(int status, String data) {
        Map<String, Object> res = new HashMap<>();
        if (SUCCESS_RESPONSE.contains(status)) {
            if (data.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(data);
                res.put("data", jsonObject.toMap());
            } else if (data.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(data);
                res.put("data", jsonArray.toList());
            }
        } else {
            res.put("data", data);
        }
        if (SUCCESS_RESPONSE.contains(status)) {
            res.put("ErrorDesc", "Thanh cong");
            res.put("ErrorCode", "0");
            res.put("status", "OK");
        } else {
            res.put("ErrorDesc", "That bai");
            res.put("ErrorCode", "-1");
            res.put("status", "FAILED");
        }
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }

    public static ResponseEntity<InputStreamResource> Response_file(int status, InputStreamResource inputStreamResource,
            String filename) {
        if (SUCCESS_RESPONSE.contains(status)) {
            return ResponseEntity.status(status)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResource);
        } else {
            return ResponseEntity.status(status)
                    .body(null);
        }
    }

    public ResponseEntity<Object> response() {
        Boolean isStatusThanhcong = Arrays.asList(RESPONSE_STATUS_CODE.THANH_CONG).contains(this.statusCode);
        CustomResponseData<T> responseData = new CustomResponseData<>();
        if (isStatusThanhcong) {
            return new ResponseEntity<>(responseData, HttpStatus.valueOf(this.statusCode));
        } else {
            return new ResponseEntity<>(responseData.error(), HttpStatus.valueOf(this.statusCode));
        }
    }

    public ResponseEntity<Object> response(T data) {
        Boolean isStatusThanhcong = Arrays.asList(RESPONSE_STATUS_CODE.THANH_CONG).contains(this.statusCode);
        CustomResponseData<T> responseData = new CustomResponseData<>(data);
        if (isStatusThanhcong) {
            return new ResponseEntity<>(responseData, HttpStatus.valueOf(this.statusCode));
        } else {
            return new ResponseEntity<>(responseData.error(data), HttpStatus.valueOf(this.statusCode));
        }
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public CustomResponseData<T> getResponseData() {
        return this.responseData;
    }

    public void setResponseData(CustomResponseData<T> responseData) {
        this.responseData = responseData;
    }

}
