package vn.ript.ssadapter.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponse<T> {

    public interface RESPONSE_STATUS_CODE {
        Integer[] THANH_CONG = { 200, 201, 202, 203, 204, 205, 206 };
        Integer[] THAT_BAI = { 400, 401, 403, 404, 405, 406, 414, 415, 422, 500, 501, 502, 503, 504 };
    }

    Integer statusCode;
    CustomResponseData<T> responseData;

    public CustomResponse(int status) {
        this.statusCode = status;
    }

    public static ResponseEntity<Map<String, Object>> Response_no_data (int status) {
        Map<String, Object> res = new HashMap<>();
        res.put("ErrorDesc", "Thanh cong");
        res.put("ErrorCode", "0");
        res.put("status", "OK");
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }
    public static ResponseEntity<Map<String, Object>> Response_data (int status, Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("ErrorDesc", "Thanh cong");
        res.put("ErrorCode", "0");
        res.put("status", "OK");
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
    }
    public static ResponseEntity<Map<String, Object>> Response_data (int status, String data) {
        Map<String, Object> res = new HashMap<>();
        res.put("data", data);
        res.put("ErrorDesc", "Thanh cong");
        res.put("ErrorCode", "0");
        res.put("status", "OK");
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.valueOf(status));
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
