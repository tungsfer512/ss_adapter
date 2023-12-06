package vn.ript.ssadapter.controller.initialize;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/tokens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TokenController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllToken() {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
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
    public ResponseEntity<Map<String, Object>> getTokenById(@PathVariable Integer id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
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

    @PutMapping("/{id}/login")
    public ResponseEntity<Map<String, Object>> loginTokenById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id + "/login";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            String password = (String) body.get("password");

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("password", password);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
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

    @PutMapping("/{id}/logout")
    public ResponseEntity<Map<String, Object>> logoutTokenById(
            @PathVariable Integer id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id + "/logout";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
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

    @PostMapping("/{id}/keys")
    public ResponseEntity<Map<String, Object>> addAuthSignKey(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id + "/keys";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            String key_label = (String) body.get("key_label");
            Map<String, Object> csr_generate_request_tmp = (Map<String, Object>) body.get("csr_generate_request");
            String key_usage_type = (String) csr_generate_request_tmp.get("key_usage_type");
            String ca_name = (String) csr_generate_request_tmp.get("ca_name");
            String csr_format = (String) csr_generate_request_tmp.get("csr_format");
            Map<String, String> subject_field_values_tmp = (Map<String, String>) csr_generate_request_tmp
                    .get("subject_field_values");

            JSONObject subject_field_values = new JSONObject();
            subject_field_values.put("C", subject_field_values_tmp.get("C"));
            subject_field_values.put("CN", subject_field_values_tmp.get("CN"));
            subject_field_values.put("serialNumber", subject_field_values_tmp.get("serialNumber"));
            subject_field_values.put("O", subject_field_values_tmp.get("O"));
            JSONObject csr_generate_request = new JSONObject();
            csr_generate_request.put("key_usage_type", key_usage_type);
            csr_generate_request.put("ca_name", ca_name);
            csr_generate_request.put("csr_format", csr_format);
            csr_generate_request.put("subject_field_values", subject_field_values);

            if (key_usage_type.equalsIgnoreCase("SIGNING")) {
                String member_id = (String) csr_generate_request_tmp.get("member_id");
                csr_generate_request.put("member_id", member_id);
            }

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("key_label", key_label);
            jsonPostObject.put("csr_generate_request", csr_generate_request);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
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

    @PostMapping("/{id}/keys-with-csrs")
    public ResponseEntity<Map<String, Object>> addAuthSignKeyWithCsrs(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id + "/keys-with-csrs";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"));

            String key_label = (String) body.get("key_label");
            Map<String, Object> csr_generate_request_tmp = (Map<String, Object>) body.get("csr_generate_request");
            String key_usage_type = (String) csr_generate_request_tmp.get("key_usage_type");
            String ca_name = (String) csr_generate_request_tmp.get("ca_name");
            String csr_format = (String) csr_generate_request_tmp.get("csr_format");
            Map<String, String> subject_field_values_tmp = (Map<String, String>) csr_generate_request_tmp
            .get("subject_field_values");
            
            JSONObject subject_field_values = new JSONObject();
            subject_field_values.put("C", subject_field_values_tmp.get("C"));
            subject_field_values.put("CN", subject_field_values_tmp.get("CN"));
            subject_field_values.put("serialNumber", subject_field_values_tmp.get("serialNumber"));
            subject_field_values.put("O", subject_field_values_tmp.get("O"));
            JSONObject csr_generate_request = new JSONObject();
            csr_generate_request.put("key_usage_type", key_usage_type);
            csr_generate_request.put("ca_name", ca_name);
            csr_generate_request.put("csr_format", csr_format);
            csr_generate_request.put("subject_field_values", subject_field_values);

            if (key_usage_type.equalsIgnoreCase("SIGNING")) {
                String member_id = (String) csr_generate_request_tmp.get("member_id");
                csr_generate_request.put("member_id", member_id);
            }
            
            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("key_label", key_label);
            jsonPostObject.put("csr_generate_request", csr_generate_request);
            
            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
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
