package vn.ript.ssadapter.controller.initialize;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/member-classes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MemberClassController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllMemberClass() {
        try {
            String url = Utils.SS_CONFIG_URL + "/member-classes";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMemberClassByCSId(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/member-classes/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }
}
