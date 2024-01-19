package vn.ript.mediator.controller.initialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.ript.mediator.model.Organization;
import vn.ript.mediator.model.initialize.Endpoint;
import vn.ript.mediator.model.initialize.Service;
import vn.ript.mediator.service.OrganizationService;
import vn.ript.mediator.service.initialize.EndpointService;
import vn.ript.mediator.service.initialize.ServiceDescriptionService;
import vn.ript.mediator.service.initialize.ServiceService;
import vn.ript.mediator.utils.CustomHttpRequest;
import vn.ript.mediator.utils.CustomResponse;
import vn.ript.mediator.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/services")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServiceController {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    ServiceDescriptionService serviceDescriptionService;

    @Autowired
    ServiceService serviceService;

    @Autowired
    EndpointService endpointService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/services/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                Gson gson = new Gson();
                JSONObject jsonService = new JSONObject(jsonResponse);
                Optional<Service> checkService = serviceService.findBySsId(jsonService.getString("id"));
                if (checkService.isPresent()) {
                    Service currentService = checkService.get();
                    System.out.println(currentService);
                    List<Object> objectEndpoints = jsonService.getJSONArray("endpoints").toList();
                    List<Endpoint> endpoints = currentService.getEndpoints();
                    JSONArray jsonEndpoints = new JSONArray();
                    for (Object objectEndpoint : objectEndpoints) {
                        JSONObject jsonEndpoint = new JSONObject(gson.toJson(objectEndpoint));
                        Endpoint currentEndpoint = null;
                        for (Endpoint endpoint : endpoints) {
                            if (endpoint.getSsId().equalsIgnoreCase(jsonEndpoint.getString("id"))) {
                                currentEndpoint = endpoint;
                                break;
                            }
                        }
                        if (currentEndpoint != null) {
                            System.out.println(currentEndpoint);
                            System.out.println("add data to endpoint");
                            JSONObject adapter_data_endpoint = new JSONObject();
                            adapter_data_endpoint.put("name", currentEndpoint.getName());
                            adapter_data_endpoint.put("description", currentEndpoint.getDescription());
                            adapter_data_endpoint.put("inputDescription",
                                    currentEndpoint.getInputDescription());
                            adapter_data_endpoint.put("outputDescription",
                                    currentEndpoint.getOutputDescription());
                            jsonEndpoint.put("adapter_data", adapter_data_endpoint);
                        } else {
                            jsonEndpoint.put("adapter_data", new JSONObject());
                        }
                        jsonEndpoints.put(jsonEndpoint);
                    }

                    jsonService.put("endpoints", jsonEndpoints);
                    System.out.println("add data to service");
                    JSONObject adapter_data_service = new JSONObject();
                    adapter_data_service.put("description", currentService.getDescription());
                    jsonService.put("adapter_data", adapter_data_service);
                } else {
                    jsonService.put("adapter_data", new JSONObject());
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonService.toMap());
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
            String url = Utils.SS_CONFIG_URL + "/services/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            if (!body.containsKey("ignore_warnings") ||
                    !body.containsKey("ssl_auth") ||
                    !body.containsKey("ssl_auth_all") ||
                    !body.containsKey("timeout") ||
                    !body.containsKey("timeout_all") ||
                    !body.containsKey("url") ||
                    !body.containsKey("url_all") ||
                    !body.containsKey("adapter_data")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            Boolean ignore_warnings = (Boolean) body.get("ignore_warnings");
            Boolean ssl_auth = (Boolean) body.get("ssl_auth");
            Boolean ssl_auth_all = (Boolean) body.get("ssl_auth_all");
            Integer timeout = (Integer) body.get("timeout");
            Boolean timeout_all = (Boolean) body.get("timeout_all");
            String url_tmp = (String) body.get("url");
            Boolean url_all = (Boolean) body.get("url_all");
            Map<String, String> adapter_data_tmp = (Map<String, String>) body.get("adapter_data");
            if (!adapter_data_tmp.containsKey("description") ||
                    !adapter_data_tmp.containsKey("isPublic") ||
                    !adapter_data_tmp.containsKey("isForCitizen") ||
                    !adapter_data_tmp.containsKey("type")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("ignore_warnings", ignore_warnings);
            jsonPostObject.put("ssl_auth", ssl_auth);
            jsonPostObject.put("ssl_auth_all", ssl_auth_all);
            jsonPostObject.put("timeout", timeout);
            jsonPostObject.put("timeout_all", timeout_all);
            jsonPostObject.put("url", url_tmp);
            jsonPostObject.put("url_all", url_all);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);
            CustomHttpRequest httpRequest = new CustomHttpRequest("PATCH", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonService = new JSONObject(jsonResponse);
                Optional<Service> checkService = serviceService.findBySsId(jsonService.getString("id"));
                if (checkService.isPresent()) {
                    Service service = checkService.get();
                    service.setDescription(adapter_data_tmp.get("description"));
                    service.setIsPublic(Boolean.valueOf(adapter_data_tmp.get("isPublic")));
                    service.setIsForCitizen(Boolean.valueOf(adapter_data_tmp.get("isForCitizen")));
                    service.setType(adapter_data_tmp.get("type"));
                    Service serviceRes = serviceService.save(service);
                    JSONObject adapter_data_service = new JSONObject();
                    adapter_data_service.put("description", serviceRes.getDescription());
                    jsonService.put("adapter_data", adapter_data_service);
                } else {
                    jsonService.put("adapter_data", new JSONObject());
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonService.toMap());
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }

    }

    @PostMapping("/{id}/endpoints")
    public ResponseEntity<Map<String, Object>> addEndpoint(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/services/" + id + "/endpoints";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            if (!body.containsKey("method") ||
                    !body.containsKey("path") ||
                    !body.containsKey("service_code") ||
                    !body.containsKey("adapter_data")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String method = (String) body.get("method");
            String path = (String) body.get("path");
            String service_code = (String) body.get("service_code");
            Map<String, String> adapter_data_tmp = (Map<String, String>) body.get("adapter_data");
            if (!adapter_data_tmp.containsKey("name") ||
                    !adapter_data_tmp.containsKey("description") ||
                    !adapter_data_tmp.containsKey("inputDescription") ||
                    !adapter_data_tmp.containsKey("outputDescription") ||
                    !adapter_data_tmp.containsKey("isPublic") ||
                    !adapter_data_tmp.containsKey("isForCitizen") ||
                    !adapter_data_tmp.containsKey("type")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("method", method);
            jsonPostObject.put("path", path);
            jsonPostObject.put("service_code", service_code);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);
            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonEndpoint = new JSONObject(jsonResponse);
                Optional<Service> checkService = serviceService.findBySsId(id);
                if (checkService.isPresent()) {
                    Service service = checkService.get();
                    Endpoint endpoint = new Endpoint();
                    endpoint.setSsId(jsonEndpoint.getString("id"));
                    endpoint.setName(adapter_data_tmp.get("name"));
                    endpoint.setMethod(method);
                    endpoint.setPath(path);
                    endpoint.setDescription(adapter_data_tmp.get("description"));
                    endpoint.setInputDescription(adapter_data_tmp.get("inputDescription"));
                    endpoint.setOutputDescription(adapter_data_tmp.get("outputDescription"));
                    endpoint.setIsPublic(Boolean.valueOf(adapter_data_tmp.get("isPublic")));
                    endpoint.setIsForCitizen(Boolean.valueOf(adapter_data_tmp.get("isForCitizen")));
                    endpoint.setType(adapter_data_tmp.get("type"));
                    Endpoint endpointRes = endpointService.save(endpoint);
                    List<Endpoint> endpoints = service.getEndpoints();
                    endpoints.add(endpointRes);
                    service.setEndpoints(endpoints);
                    serviceService.save(service);
                    jsonEndpoint.put("adapter_data", endpointRes);
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

    @GetMapping("/{id}/service-clients")
    public ResponseEntity<Map<String, Object>> getAllServiceClient(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/services/" + id + "/service-clients";
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
    public ResponseEntity<Map<String, Object>> addServiceClients(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("items")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String url = Utils.SS_CONFIG_URL + "/services/" + id + "/service-clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            List<Map<String, String>> items = (List<Map<String, String>>) body.get("items");
            JSONArray jsonPostArray = new JSONArray();
            for (Map<String, String> item : items) {
                if (!body.containsKey("id") ||
                        !body.containsKey("name") ||
                        !body.containsKey("service_client_type")) {
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

    @PostMapping("/{id}/service-clients/delete")
    public ResponseEntity<Map<String, Object>> deleteAddServiceClients(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("items")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String url = Utils.SS_CONFIG_URL + "/services/" + id + "/service-clients/delete";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            List<Map<String, String>> items = (List<Map<String, String>>) body.get("items");
            JSONArray jsonPostArray = new JSONArray();
            for (Map<String, String> item : items) {
                if (!body.containsKey("id") ||
                        !body.containsKey("name") ||
                        !body.containsKey("service_client_type")) {
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
