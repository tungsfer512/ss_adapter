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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.ript.mediator.model.Organization;
import vn.ript.mediator.model.initialize.Endpoint;
import vn.ript.mediator.model.initialize.Service;
import vn.ript.mediator.model.initialize.ServiceDescription;
import vn.ript.mediator.service.OrganizationService;
import vn.ript.mediator.service.initialize.ServiceDescriptionService;
import vn.ript.mediator.service.initialize.ServiceService;
import vn.ript.mediator.utils.CustomHttpRequest;
import vn.ript.mediator.utils.CustomResponse;
import vn.ript.mediator.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/service-descriptions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServiceDescriptionController {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    ServiceDescriptionService serviceDescriptionService;

    @Autowired
    ServiceService serviceService;

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/service-descriptions/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                Gson gson = new Gson();

                JSONObject jsonServiceDescription = new JSONObject(jsonResponse);
                Optional<ServiceDescription> checkCurrentServiceDescription = serviceDescriptionService
                        .findBySsId(id.toString());
                if (checkCurrentServiceDescription.isPresent()) {
                    ServiceDescription currentServiceDescription = checkCurrentServiceDescription.get();
                    List<Object> objectServices = jsonServiceDescription.getJSONArray("services").toList();
                    List<Service> services = currentServiceDescription.getServices();
                    JSONArray jsonServices = new JSONArray();
                    for (Object objectService : objectServices) {
                        JSONObject jsonService = new JSONObject(gson.toJson(objectService));
                        Service currentService = null;
                        for (Service service : services) {
                            if (service.getSsId().equalsIgnoreCase(jsonService.getString("id"))) {
                                currentService = service;
                                break;
                            }
                        }
                        if (currentService != null) {
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
                            JSONObject adapter_data_service = new JSONObject();
                            adapter_data_service.put("description", currentService.getDescription());
                            jsonService.put("adapter_data", adapter_data_service);
                        } else {
                            jsonService.put("adapter_data", new JSONObject());
                        }
                        jsonServices.put(jsonService);
                    }

                    jsonServiceDescription.put("services", jsonServices);
                    JSONObject adapter_data_service_description = new JSONObject();
                    adapter_data_service_description.put("description", currentServiceDescription.getDescription());
                    jsonServiceDescription.put("adapter_data", adapter_data_service_description);
                } else {
                    jsonServiceDescription.put("adapter_data", new JSONObject());
                }

                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonServiceDescription.toMap());
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
            String url = Utils.SS_CONFIG_URL + "/service-descriptions/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            if (!body.containsKey("type") ||
                    !body.containsKey("url") ||
                    !body.containsKey("rest_service_code") ||
                    !body.containsKey("new_rest_service_code") ||
                    !body.containsKey("ignore_warnings") ||
                    !body.containsKey("adapter_data")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

            String type = (String) body.get("type");
            String url_tmp = (String) body.get("url");
            String rest_service_code = (String) body.get("rest_service_code");
            String new_rest_service_code = (String) body.get("new_rest_service_code");
            Boolean ignore_warnings = (Boolean) body.get("ignore_warnings");
            Map<String, Object> adapter_data_tmp = (Map<String, Object>) body.get("adapter_data");
            if (!adapter_data_tmp.containsKey("service_description")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }
            Map<String, String> adapter_data_tmp_service_description = (Map<String, String>) adapter_data_tmp
                    .get("service_description");
            if (!adapter_data_tmp_service_description.containsKey("description")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("type", type);
            jsonPostObject.put("url", url_tmp);
            jsonPostObject.put("rest_service_code", rest_service_code);
            jsonPostObject.put("new_rest_service_code", new_rest_service_code);
            jsonPostObject.put("ignore_warnings", ignore_warnings);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);
            CustomHttpRequest httpRequest = new CustomHttpRequest("PATCH", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                String subsystem_code = Utils.SS_MANAGE_ID.replace(':', '/');
                String xRoadClient = Utils.SS_ID.replace(':', '/');
                String urlManage = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                        "/" + Utils.SS_MANAGE_SERVICE_CODE + "/service-descriptions";
                Map<String, String> headersManage = new HashMap<>();
                headersManage.put("X-Road-Client", xRoadClient);

                JSONObject jsonPostObjectManage = new JSONObject();
                jsonPostObjectManage.put("ssId", adapter_data_tmp_service_description.get("ssId"));
                jsonPostObjectManage.put("description", adapter_data_tmp_service_description.get("description"));
                StringEntity entityManage = new StringEntity(jsonPostObjectManage.toString(),
                        ContentType.APPLICATION_JSON);

                CustomHttpRequest httpRequestManage = new CustomHttpRequest("PATCH", urlManage, headersManage);
                HttpResponse httpResponseManage = httpRequestManage.request(entityManage);
                if (httpResponseManage.getStatusLine().getStatusCode() == 200) {
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    Optional<Organization> checkOrganization = organizationService
                            .findBySsId(jsonObject.getString("client_id"));
                    if (checkOrganization.isPresent()) {
                        Optional<ServiceDescription> checkServiceDescription = serviceDescriptionService
                                .findBySsId(jsonObject.getString("id"));
                        if (checkServiceDescription.isPresent()) {
                            ServiceDescription serviceDescription = checkServiceDescription.get();
                            serviceDescription.setDescription(adapter_data_tmp_service_description.get("description"));
                            ServiceDescription serviceDescriptionRes = serviceDescriptionService
                                    .save(serviceDescription);
                            return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                                    serviceDescriptionRes);
                        } else {
                            return CustomResponse.Response_data(404, "Khong tim thay service description !");
                        }
                    } else {
                        return CustomResponse.Response_data(404, "Khong tim thay don vi !");
                    }
                } else {
                    return CustomResponse.Response_data(httpResponseManage.getStatusLine().getStatusCode(),
                            httpResponseManage.toString());
                }
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
    public ResponseEntity<Map<String, Object>> deleteById(@PathVariable Integer id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/service-descriptions/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("DELETE", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                Optional<ServiceDescription> checkServiceDescription = serviceDescriptionService
                        .findBySsId(id.toString());
                if (checkServiceDescription.isPresent()) {
                    ServiceDescription serviceDescription = checkServiceDescription.get();
                    serviceDescriptionService.deleteById(serviceDescription.getId());
                }
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

    @PutMapping("/{id}/enable")
    public ResponseEntity<Map<String, Object>> enableById(@PathVariable Integer id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/service-descriptions/" + id + "/enable";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
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

    @PutMapping("/{id}/disable")
    public ResponseEntity<Map<String, Object>> disableById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/service-descriptions/" + id + "/disable";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            if (!body.containsKey("disabled_notice")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("disabled_notice", body.get("disabled_notice"));

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
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
