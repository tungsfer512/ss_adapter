package vn.ript.ssadapter.controller.initialize;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.google.gson.Gson;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.initialize.Endpoint;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.service.initialize.EndpointService;
import vn.ript.ssadapter.service.initialize.ServiceDescriptionService;
import vn.ript.ssadapter.service.initialize.ServiceService;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/endpoints")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EndpointController {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    ServiceDescriptionService serviceDescriptionService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    EndpointService endpointService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
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
                JSONObject jsonEndpoint = new JSONObject(jsonResponse);
                Optional<Endpoint> checkEndpoint = endpointService.findBySsId(id.toString());
                if (checkEndpoint.isPresent()) {
                    jsonEndpoint.put("adapter_data", checkEndpoint.get());
                } else {
                    jsonEndpoint.put("adapter_data", new JSONObject());
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonEndpoint.toMap());
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
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            if (!body.containsKey("generated") ||
                    !body.containsKey("id") ||
                    !body.containsKey("method") ||
                    !body.containsKey("path") ||
                    !body.containsKey("service_code") ||
                    !body.containsKey("adapter_data")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            Boolean generated = (Boolean) body.get("generated");
            String id_tmp = (String) body.get("id");
            String method = (String) body.get("method");
            String path = (String) body.get("path");
            String service_code = (String) body.get("service_code");
            Map<String, String> adapter_data_tmp = (Map<String, String>) body.get("adapter_data");
            if (!adapter_data_tmp.containsKey("name") ||
                    !adapter_data_tmp.containsKey("description") ||
                    !adapter_data_tmp.containsKey("inputDescription") ||
                    !adapter_data_tmp.containsKey("outputDescription")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("generated", generated);
            jsonPostObject.put("id", id_tmp);
            jsonPostObject.put("method", method);
            jsonPostObject.put("path", path);
            jsonPostObject.put("service_code", service_code);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PATCH", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonEndpoint = new JSONObject(jsonResponse);
                Optional<Endpoint> checkEndpoint = endpointService.findBySsId(id.toString());
                if (checkEndpoint.isPresent()) {
                    Endpoint endpoint = checkEndpoint.get();
                    endpoint.setName(adapter_data_tmp.get("name"));
                    endpoint.setMethod(method);
                    endpoint.setPath(path);
                    endpoint.setDescription(adapter_data_tmp.get("description"));
                    endpoint.setInputDescription(adapter_data_tmp.get("inputDescription"));
                    endpoint.setOutputDescription(adapter_data_tmp.get("outputDescription"));
                    Endpoint endpointRes = endpointService.save(endpoint);
                    jsonEndpoint.put("adapter_data", endpointRes);
                } else {
                    jsonEndpoint.put("adapter_data", new JSONObject());
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonEndpoint.toMap());
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/service-clients")
    public ResponseEntity<Map<String, Object>> getAllServiceClientOfEndpoint(
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
                List<Object> clients = new JSONArray(jsonResponse).toList();
                JSONArray jsonArray = new JSONArray();
                for (Object client : clients) {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(gson.toJson(client));
                    Optional<Organization> checkOrganization = organizationService
                            .findBySsId(jsonObject.getString("id"));
                    if (checkOrganization.isPresent()) {
                        Organization organization = checkOrganization.get();
                        jsonObject.put("adapter_data", organization);
                    } else {
                        jsonObject.put("adapter_data", new JSONObject());
                    }
                    jsonArray.put(jsonObject);
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonArray.toList());
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/service-clients")
    public ResponseEntity<Map<String, Object>> addServiceClientsOfEndpoint(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("items")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id + "/service-clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            List<Map<String, String>> items = (List<Map<String, String>>) body.get("items");
            JSONArray jsonPostArray = new JSONArray();
            for (Map<String, String> item : items) {
                if (!item.containsKey("id") ||
                        !item.containsKey("name") ||
                        !item.containsKey("service_client_type")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin!");
                }
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/service-clients/delete")
    public ResponseEntity<Map<String, Object>> deleteServiceClientsOfEndpoint(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("items")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String url = Utils.SS_CONFIG_URL + "/endpoints/" + id + "/service-clients/delete";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            List<Map<String, String>> items = (List<Map<String, String>>) body.get("items");
            JSONArray jsonPostArray = new JSONArray();
            for (Map<String, String> item : items) {
                if (!item.containsKey("id") ||
                        !item.containsKey("name") ||
                        !item.containsKey("service_client_type")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin!");
                }
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
