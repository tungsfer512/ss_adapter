package vn.ript.ssadapter.controller.initialize;

import java.util.Map;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/endpoints")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EndpointController {

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEndpointById(@PathVariable Integer id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id;
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

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editEndpointById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            Boolean generated = (Boolean) body.get("generated");
            String id_tmp = (String) body.get("id");
            String method = (String) body.get("method");
            String path = (String) body.get("path");
            String service_code = (String) body.get("service_code");

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("generated", generated);
            jsonPostObject.put("id_tmp", id_tmp);
            jsonPostObject.put("method", method);
            jsonPostObject.put("path", path);
            jsonPostObject.put("service_code", service_code);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PATCH", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEndpointById(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("DELETE", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 204) {

                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/service-clients")
    public ResponseEntity<Map<String, Object>> getAllServiceClient(
            @PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id + "/service-clients";
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

    @PostMapping("/{id}/service-clients")
    public ResponseEntity<Map<String, Object>> addServiceClients(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id + "/service-clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            List<Map<String, String>> items = (List<Map<String, String>>) body.get("items");
            JSONArray jsonPostArray = new JSONArray();
            for (Map<String, String> item : items) {
                String id_tmp = item.get("id");
                String name = item.get("name");
                String service_client_type = item.get("service_client_type");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", id_tmp);
                jsonObject.put("name", name);
                jsonObject.put("service_client_type", service_client_type);
                jsonPostArray.put(jsonObject);
            }
            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("items", jsonPostArray);
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

    @PostMapping("/{id}/service-clients/delete")
    public ResponseEntity<Map<String, Object>> deleteAddServiceClients(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id + "/service-clients/delete";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            List<Map<String, String>> items = (List<Map<String, String>>) body.get("items");
            JSONArray jsonPostArray = new JSONArray();
            for (Map<String, String> item : items) {
                String id_tmp = item.get("id");
                String name = item.get("name");
                String service_client_type = item.get("service_client_type");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", id_tmp);
                jsonObject.put("name", name);
                jsonObject.put("service_client_type", service_client_type);
                jsonPostArray.put(jsonObject);
            }
            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("items", jsonPostArray);
            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
