package vn.ript.mediator.controller.initialize;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.mediator.utils.CustomHttpRequest;
import vn.ript.mediator.utils.CustomResponse;
import vn.ript.mediator.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/tokens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TokenController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll() {
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("name")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }
            String name = (String) body.get("name");
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("name", name);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PATCH", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
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

    @PutMapping("/{id}/pin")
    public ResponseEntity<Map<String, Object>> changePinById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("old_pin") || !body.containsKey("new_pin")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }
            String old_pin = (String) body.get("old_pin");
            String new_pin = (String) body.get("new_pin");
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id + "/pin";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("old_pin", old_pin);
            jsonPostObject.put("new_pin", new_pin);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/login")
    public ResponseEntity<Map<String, Object>> loginById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id + "/login";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));
            if (!body.containsKey("password")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/logout")
    public ResponseEntity<Map<String, Object>> logoutById(
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/keys")
    public ResponseEntity<Map<String, Object>> addAuthSignKeyOfToken(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id + "/keys";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));
            if (!body.containsKey("key_label") ||
                    !body.containsKey("csr_generate_request")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }
            String key_label = (String) body.get("key_label");
            Map<String, Object> csr_generate_request_tmp = (Map<String, Object>) body.get("csr_generate_request");
            if (!csr_generate_request_tmp.containsKey("key_usage_type") ||
                    !csr_generate_request_tmp.containsKey("ca_name") ||
                    !csr_generate_request_tmp.containsKey("csr_format") ||
                    !csr_generate_request_tmp.containsKey("subject_field_values")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

            String key_usage_type = (String) csr_generate_request_tmp.get("key_usage_type");
            String ca_name = (String) csr_generate_request_tmp.get("ca_name");
            String csr_format = (String) csr_generate_request_tmp.get("csr_format");
            Map<String, String> subject_field_values_tmp = (Map<String, String>) csr_generate_request_tmp
                    .get("subject_field_values");
            if (!subject_field_values_tmp.containsKey("C") ||
                    !subject_field_values_tmp.containsKey("CN") ||
                    !subject_field_values_tmp.containsKey("serialNumber") ||
                    !subject_field_values_tmp.containsKey("O")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

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
                if (!csr_generate_request_tmp.containsKey("member_id")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin");
                }
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/keys-with-csrs")
    public ResponseEntity<Map<String, Object>> addAuthSignKeyWithCsrsOfToken(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/tokens/" + id + "/keys-with-csrs";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"));
            if (!body.containsKey("key_label") ||
                    !body.containsKey("csr_generate_request")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }
            String key_label = (String) body.get("key_label");
            Map<String, Object> csr_generate_request_tmp = (Map<String, Object>) body.get("csr_generate_request");
            if (!csr_generate_request_tmp.containsKey("key_usage_type") ||
                    !csr_generate_request_tmp.containsKey("ca_name") ||
                    !csr_generate_request_tmp.containsKey("csr_format") ||
                    !csr_generate_request_tmp.containsKey("subject_field_values")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

            String key_usage_type = (String) csr_generate_request_tmp.get("key_usage_type");
            String ca_name = (String) csr_generate_request_tmp.get("ca_name");
            String csr_format = (String) csr_generate_request_tmp.get("csr_format");
            Map<String, String> subject_field_values_tmp = (Map<String, String>) csr_generate_request_tmp
                    .get("subject_field_values");
            if (!subject_field_values_tmp.containsKey("C") ||
                    !subject_field_values_tmp.containsKey("CN") ||
                    !subject_field_values_tmp.containsKey("serialNumber") ||
                    !subject_field_values_tmp.containsKey("O")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

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
                if (!csr_generate_request_tmp.containsKey("member_id")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin");
                }
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }
}
