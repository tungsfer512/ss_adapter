package vn.ript.ssadapter.controller.initialize;

import java.util.ArrayList;
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

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.initialize.Endpoint;
import vn.ript.ssadapter.model.initialize.Service;
import vn.ript.ssadapter.model.initialize.ServiceDescription;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.service.initialize.ServiceDescriptionService;
import vn.ript.ssadapter.service.initialize.ServiceService;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

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
    public ResponseEntity<Map<String, Object>> getServiceDescriptionById(@PathVariable Integer id) {
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
                        jsonServices.put(jsonService);
                    }

                    jsonServiceDescription.put("services", jsonServices);
                    System.out.println("add data to service description");
                    JSONObject adapter_data_service_description = new JSONObject();
                    adapter_data_service_description.put("description", currentServiceDescription.getDescription());
                    jsonServiceDescription.put("adapter_data", adapter_data_service_description);
                } else {
                    jsonServiceDescription.put("adapter_data", new JSONObject());
                }

                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonServiceDescription.toMap());
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> patchServiceDescriptionById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/service-descriptions/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));
            String type = (String) body.get("type");
            String url_tmp = (String) body.get("url");
            String rest_service_code = (String) body.get("rest_service_code");
            String new_rest_service_code = (String) body.get("new_rest_service_code");
            Boolean ignore_warnings = (Boolean) body.get("ignore_warnings");
            Map<String, Object> adapter_data_tmp = (Map<String, Object>) body.get("adapter_data");
            Map<String, String> adapter_data_tmp_service_description = (Map<String, String>) adapter_data_tmp
                    .get("service_description");
            Map<String, String> adapter_data_tmp_service = (Map<String, String>) adapter_data_tmp.get("service");

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
                Gson gson = new Gson();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                Optional<Organization> checkOrganization = organizationService
                        .findByOrganId(jsonObject.getString("client_id"));
                if (checkOrganization.isPresent()) {
                    Optional<ServiceDescription> checkServiceDescription = serviceDescriptionService.findBySsId(jsonObject.getString("id"));
                    if (checkServiceDescription.isPresent()) {
                        ServiceDescription serviceDescription = checkServiceDescription.get();
                        serviceDescription.setDescription(adapter_data_tmp_service_description.get("description"));
    
                        List<Service> services_tmp = serviceDescription.getServices();
                        if (!type.equalsIgnoreCase("WSDL")) {
                            Service service = services_tmp.get(0);
                            String serviceSsId = jsonObject.getString("client_id") + ":"
                                    + ((String) body.get("new_rest_service_code"));
                            service.setSsId(serviceSsId);
                            service.setDescription(adapter_data_tmp_service.get("description"));
                            Service serviceRes = serviceService.save(service);
    
                            ServiceDescription serviceDescriptionRes = serviceDescriptionService.save(serviceDescription);
    
                            JSONObject adapter_data_service_description = new JSONObject();
                            adapter_data_service_description.put("description", serviceDescriptionRes.getDescription());
                            jsonObject.put("adapter_data", adapter_data_service_description);
    
                            List<Object> objectServices = jsonObject.getJSONArray("services").toList();
                            JSONArray jsonServices = new JSONArray();
                            JSONObject jsonService = new JSONObject(gson.toJson(objectServices.get(0)));
                            System.out.println("add data to service");
                            JSONObject adapter_data_service = new JSONObject();
                            adapter_data_service.put("description", serviceRes.getDescription());
                            jsonService.put("adapter_data", adapter_data_service);
                            jsonServices.put(jsonService);
    
                            System.out.println("add data to service description");
                            jsonObject.put("services", jsonServices);
                        }
                    }
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonObject.toMap());
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteServiceDescriptionById(@PathVariable Integer id) {
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
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Map<String, Object>> enableServiceDescriptionById(@PathVariable Integer id) {
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
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Map<String, Object>> disableServiceDescriptionById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/service-descriptions/" + id + "/disable";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("disabled_notice", body.get("disabled_notice"));

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
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

}
