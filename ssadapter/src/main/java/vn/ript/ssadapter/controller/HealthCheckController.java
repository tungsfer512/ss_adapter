package vn.ript.ssadapter.controller;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/healthcheck")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HealthCheckController {

    @GetMapping(path = "")
    public ResponseEntity<Map<String, Object>> healthcheck() {

        String urlAlert = "https://" + Utils.SS_CONFIG_URL + "/notifications/alerts";
        CustomHttpRequest customHttpRequestAlert = new CustomHttpRequest("GET", urlAlert);
        HttpResponse httpResponseAlert = customHttpRequestAlert.request();

        String urlSessionStatus = "https://" + Utils.SS_CONFIG_URL + "/notifications/session-status";
        CustomHttpRequest customHttpRequestSessionStatus = new CustomHttpRequest("GET", urlSessionStatus);
        HttpResponse httpResponseSessionStatus = customHttpRequestSessionStatus.request();

        if (httpResponseSessionStatus.getStatusLine().getStatusCode() == 200) {
            if (httpResponseAlert.getStatusLine().getStatusCode() == 200) {
                return CustomResponse.Response_no_data(200);

            } else {
                return CustomResponse.Response_data(httpResponseAlert.getStatusLine().getStatusCode(),
                        httpResponseAlert.toString());
            }
        } else {
            return CustomResponse.Response_data(httpResponseSessionStatus.getStatusLine().getStatusCode(),
                    httpResponseSessionStatus.toString());
        }
    }
}
