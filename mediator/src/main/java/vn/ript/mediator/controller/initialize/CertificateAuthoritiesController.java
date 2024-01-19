package vn.ript.mediator.controller.initialize;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.mediator.utils.CustomHttpRequest;
import vn.ript.mediator.utils.CustomResponse;
import vn.ript.mediator.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/certificate-authorities")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CertificateAuthoritiesController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll() {
        try {
            String url = Utils.SS_CONFIG_URL + "/certificate-authorities";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            httpRequest.add_query_param("include_intermediate_cas", true);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{ca_name}/csr-subject-fields")
    public ResponseEntity<Map<String, Object>> getCSRSubjectFeildsByCAName(
            @PathVariable String ca_name,
            @RequestParam(name = "key_usage_type", required = true) String key_usage_type,
            @RequestParam(name = "member_id", required = false) String member_id,
            @RequestParam(name = "is_new_member", required = false) Boolean is_new_member) {
        try {
            String ca_name_url = ca_name.replace(" ", "%20");
            String url = Utils.SS_CONFIG_URL + "/certificate-authorities/" + ca_name_url + "/csr-subject-fields";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            httpRequest.add_query_param("key_usage_type", key_usage_type);
            if (key_usage_type.equals("SIGNING")) {
                httpRequest.add_query_param("member_id", member_id);
                httpRequest.add_query_param("is_new_member", is_new_member);
            }
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
