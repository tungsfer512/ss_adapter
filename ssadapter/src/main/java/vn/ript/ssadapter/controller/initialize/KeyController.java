package vn.ript.ssadapter.controller.initialize;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/keys")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KeyController {

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/keys/" + id;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteById(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/keys/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("DELETE", url, headers);
            HttpResponse httpResponse = httpRequest.request();
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

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editById(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/keys/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("PATCH", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/csrs")
    public ResponseEntity<InputStreamResource> generateCSRSOfKey(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/keys/" + id + "/csrs";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"));

            if (!body.containsKey("key_usage_type") ||
                    !body.containsKey("ca_name") ||
                    !body.containsKey("csr_format") ||
                    !body.containsKey("subject_field_values")) {
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
                return CustomResponse.Response_file(400, null, null);
            }

            String key_usage_type = body.get("key_usage_type").toString();
            String ca_name = body.get("ca_name").toString();
            String csr_format = body.get("csr_format").toString();
            Map<String, String> subject_field_values_tmp = (Map<String, String>) body.get("subject_field_values");

            JSONObject subject_field_values = new JSONObject();
            subject_field_values.put("C", subject_field_values_tmp.get("C"));
            subject_field_values.put("CN", subject_field_values_tmp.get("CN"));
            subject_field_values.put("serialNumber",
                    subject_field_values_tmp.get("serialNumber"));
            subject_field_values.put("O", subject_field_values_tmp.get("O"));

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("key_usage_type", key_usage_type);
            jsonPostObject.put("ca_name", ca_name);
            jsonPostObject.put("csr_format", csr_format);
            jsonPostObject.put("subject_field_values", subject_field_values);

            if (key_usage_type.equalsIgnoreCase("SIGNING")) {
                String member_id = body.get("member_id").toString();
                jsonPostObject.put("member_id", member_id);
            }
            String filename = Utils.UUID() + "." + csr_format.toLowerCase();

            StringEntity entity = new StringEntity(jsonPostObject.toString(),
                    ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStreamResource inputStreamResource = new InputStreamResource(httpEntity.getContent());
                return CustomResponse.Response_file(httpResponse.getStatusLine().getStatusCode(), inputStreamResource,
                        filename);
            } else {
                System.out.println("-------------------------------------------------");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                System.out.println(jsonResponse);
                return CustomResponse.Response_file(httpResponse.getStatusLine().getStatusCode(), null, null);
            }
        } catch (Exception e) {
            return CustomResponse.Response_file(500, null, null);
        }
    }

    @GetMapping("/{key_id}/csrs/{id}")
    public ResponseEntity<InputStreamResource> downloadCSRSOfKeyById(
            @PathVariable String key_id,
            @PathVariable String id,
            @RequestParam(name = "csr_format") String csr_format) {
        try {
            String filename = Utils.UUID() + "." + csr_format.toLowerCase();
            String url = Utils.SS_CONFIG_URL + "/keys/" + key_id + "/csrs/" + id + "?csr_format="
                    + csr_format.toUpperCase();
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStreamResource inputStreamResource = new InputStreamResource(httpEntity.getContent());
                return CustomResponse.Response_file(httpResponse.getStatusLine().getStatusCode(), inputStreamResource,
                        filename);
            } else {
                return CustomResponse.Response_file(httpResponse.getStatusLine().getStatusCode(), null, null);
            }
        } catch (Exception e) {
            return CustomResponse.Response_file(500, null, null);
        }
    }

    @DeleteMapping("/{key_id}/csrs/{id}")
    public ResponseEntity<Map<String, Object>> deleteCSRSOfKeyById(
            @PathVariable String key_id,
            @PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/keys/" + key_id + "/csrs/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("DELETE", url, headers);
            HttpResponse httpResponse = httpRequest.request();
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

    @GetMapping("/{key_id}/csrs/{id}/possible-actions")
    public ResponseEntity<Map<String, Object>> getAllCSRSPosibleActionOfKeyById(
            @PathVariable String key_id,
            @PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/keys/" + key_id + "/csrs/" + id + "/possible-actions";
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

    @GetMapping("/{id}/possible-actions")
    public ResponseEntity<Map<String, Object>> getAllPosibleActionOfKeyById(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/keys/" + id + "/possible-actions";
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

}
