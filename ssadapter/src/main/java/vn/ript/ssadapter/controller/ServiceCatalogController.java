package vn.ript.ssadapter.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.initialize.Endpoint;
import vn.ript.ssadapter.model.initialize.Service;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.service.initialize.ServiceService;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/v1/sevice-catalogs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServiceCatalogController {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    ServiceService serviceService;

    @GetMapping("/listClients")
    public ResponseEntity<Map<String, Object>> listClients() {
        try {
            String url = Utils.SS_BASE_URL + "/listClients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest customHttpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = customHttpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                List<Object> members = jsonObject.getJSONArray("member").toList();
                Gson gson = new Gson();

                JSONArray jsonMembers = new JSONArray();
                for (Object member : members) {
                    JSONObject jsonMember = new JSONObject(gson.toJson(member));
                    JSONObject jsonMemberId = jsonMember.getJSONObject("id");
                    String memberId = "";
                    memberId += jsonMemberId.getString("xroad_instance") + ":";
                    memberId += jsonMemberId.getString("member_class") + ":";
                    memberId += jsonMemberId.getString("member_code");
                    if (jsonMemberId.has("subsystem_code")) {
                        memberId += ":" + jsonMemberId.getString("subsystem_code");
                    }
                    Optional<Organization> checkOrganization = organizationService
                            .findBySsId(memberId);
                    if (checkOrganization.isPresent()) {
                        Organization organization = checkOrganization.get();
                        jsonMember.put("adapter_data", organization);
                    } else {
                        jsonMember.put("adapter_data", new JSONObject());
                    }
                    jsonMembers.put(jsonMember);
                }

                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonMembers.toList());
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/rest-endpoints")
    public ResponseEntity<Map<String, Object>> listRestServiceWithEndpoint(
            @RequestHeader(value = "Subsystem-Code") String subsystemCode) {
        try {

            Gson gson = new Gson();
            // String xRoadClient = Utils.SS_ID.replace(':', '/');
            // String subsystem_code = subsystemCode.replace(':', '/');
            String xRoadClient = "CS/GOV/MANAGESS2MC/ABCABC";
            String subsystem_code = "CS/GOV/MANAGESSMC/MANAGESSSUB2";
            String urlList = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/listMethods";
            String urlAllow = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/allowedMethods";

            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"),
                    Map.entry("X-Road-Client", xRoadClient));

            CustomHttpRequest customHttpRequestList = new CustomHttpRequest("GET", urlList, headers);
            CustomHttpRequest customHttpRequestAllow = new CustomHttpRequest("GET", urlAllow, headers);

            HttpResponse httpResponseList = customHttpRequestList.request();
            HttpResponse httpResponseAllow = customHttpRequestAllow.request();

            if (httpResponseList.getStatusLine().getStatusCode() == 200) {
                if (httpResponseAllow.getStatusLine().getStatusCode() == 200) {
                    String jsonResponseList = EntityUtils.toString(httpResponseList.getEntity());
                    String jsonResponseAllow = EntityUtils.toString(httpResponseAllow.getEntity());

                    JSONObject jsonObjectServiceList = new JSONObject(jsonResponseList);
                    List<Object> objectListServices = jsonObjectServiceList.getJSONArray("service").toList();
                    JSONObject jsonObjectServiceAllow = new JSONObject(jsonResponseAllow);
                    List<Object> objectAllowedServices = jsonObjectServiceAllow.getJSONArray("service").toList();

                    JSONObject jsonListService = new JSONObject();
                    JSONArray jsonListServiceArray = new JSONArray();
                    for (Object objectListService : objectListServices) {
                        JSONObject listService = new JSONObject(gson.toJson(objectListService));
                        JSONArray jsonListEndpoints = new JSONArray();
                        List<Object> objectListEndpoints = listService.getJSONArray("endpoint_list").toList();
                        for (Object objectAllowedService : objectAllowedServices) {
                            JSONObject allowedService = new JSONObject(gson.toJson(objectAllowedService));
                            if (listService.getString("member_class")
                                    .equalsIgnoreCase(allowedService.getString("member_class"))
                                    &&
                                    listService.getString("member_code")
                                            .equalsIgnoreCase(allowedService.getString("member_code"))
                                    &&
                                    listService.getString("subsystem_code")
                                            .equalsIgnoreCase(allowedService.getString("subsystem_code"))
                                    &&
                                    listService.getString("service_code")
                                            .equalsIgnoreCase(allowedService.getString("service_code"))
                                    &&
                                    listService.getString("object_type")
                                            .equalsIgnoreCase(allowedService.getString("object_type"))
                                    &&
                                    listService.getString("service_type")
                                            .equalsIgnoreCase(allowedService.getString("service_type"))
                                    &&
                                    listService.getString("xroad_instance")
                                            .equalsIgnoreCase(allowedService.getString("xroad_instance"))) {
                                List<Object> objectAllowedEndpoints = allowedService.getJSONArray("endpoint_list")
                                        .toList();
                                for (Object objectListEndpoint : objectListEndpoints) {
                                    JSONObject listEndpoint = new JSONObject(gson.toJson(objectListEndpoint));
                                    for (Object objectAllowedEndpoint : objectAllowedEndpoints) {
                                        JSONObject allowedEndpoint = new JSONObject(gson.toJson(objectAllowedEndpoint));
                                        if (listEndpoint.getString("method")
                                                .equalsIgnoreCase(allowedEndpoint.getString("method"))
                                                &&
                                                listEndpoint.getString("path")
                                                        .equalsIgnoreCase(allowedEndpoint.getString("path"))) {
                                            listEndpoint.put("allowed", true);
                                        }
                                    }
                                    if (!listEndpoint.has("allowed")) {
                                        listEndpoint.put("allowed", false);
                                    }
                                    jsonListEndpoints.put(listEndpoint);
                                }
                            }
                        }
                        if (jsonListEndpoints.length() == 0) {
                            for (Object objectListEndpoint : objectListEndpoints) {
                                JSONObject listEndpoint = new JSONObject(gson.toJson(objectListEndpoint));
                                listEndpoint.put("allowed", false);
                                jsonListEndpoints.put(listEndpoint);
                            }
                        }

                        listService.put("endpoint_list", jsonListEndpoints);

                        String subsystem_id = listService.getString("xroad_instance") + ":"
                                + listService.getString("member_class") + ":" + listService.getString("member_code")
                                + ":" + listService.getString("subsystem_code");
                        String service_id = subsystem_id + ":" + listService.getString("service_code");
                        Optional<Service> checkService = serviceService.findBySsId(service_id);
                        if (checkService.isPresent()) {
                            Service currentService = checkService.get();
                            List<Object> objectEndpoints = jsonListEndpoints.toList();
                            List<Endpoint> endpoints = currentService.getEndpoints();
                            JSONArray jsonEndpoints = new JSONArray();
                            for (Object objectEndpoint : objectEndpoints) {
                                JSONObject jsonEndpoint = new JSONObject(gson.toJson(objectEndpoint));
                                Endpoint currentEndpoint = null;
                                for (Endpoint endpoint : endpoints) {
                                    if (endpoint.getPath().equalsIgnoreCase(jsonEndpoint.getString("path"))
                                            && endpoint.getMethod()
                                                    .equalsIgnoreCase(jsonEndpoint.getString("method"))) {
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

                            listService.put("endpoint_list", jsonEndpoints);
                            JSONObject adapter_data_service = new JSONObject();
                            adapter_data_service.put("description", currentService.getDescription());
                            listService.put("adapter_data", adapter_data_service);
                        } else {
                            listService.put("adapter_data", new JSONObject());
                        }
                        jsonListServiceArray.put(listService);
                    }
                    jsonListService.put("service", jsonListServiceArray);
                    return CustomResponse.Response_data(httpResponseAllow.getStatusLine().getStatusCode(),
                            jsonListService.toMap());
                } else {
                    return CustomResponse.Response_data(httpResponseAllow.getStatusLine().getStatusCode(),
                            httpResponseAllow.getStatusLine() + "----- Loi goi Allowed");
                }
            } else {
                return CustomResponse.Response_data(httpResponseList.getStatusLine().getStatusCode(),
                        httpResponseList.getStatusLine() + "----- Loi goi List");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/rest-endpoints/get")
    public ResponseEntity<Map<String, Object>> getEndpoints(
            @RequestHeader(value = "Subsystem-Code") String subsystemCode,
            @RequestHeader(value = "Service-Code") String serviceCode,
            @RequestHeader(value = "Service-Endpoint") String serviceEndpoint) {
        try {

            String xRoadClient = Utils.SS_ID.replace(':', '/');
            String subsystem_code = subsystemCode.replace(':', '/');
            String url = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/" + serviceCode + "/" + serviceEndpoint;

            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"),
                    Map.entry("X-Road-Client", xRoadClient));

            CustomHttpRequest customHttpRequest = new CustomHttpRequest("GET", url, headers);

            HttpResponse httpResponse = customHttpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CustomResponse.Response_data(500, e);
        }
    }

}
