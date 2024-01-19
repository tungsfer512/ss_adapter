package vn.ript.mediator.controller.initialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import vn.ript.mediator.model.Organization;
import vn.ript.mediator.model.initialize.Endpoint;
import vn.ript.mediator.model.initialize.Service;
import vn.ript.mediator.model.initialize.ServiceDescription;
import vn.ript.mediator.service.OrganizationService;
import vn.ript.mediator.service.initialize.ServiceDescriptionService;
import vn.ript.mediator.service.initialize.ServiceService;
import vn.ript.mediator.utils.Constants;
import vn.ript.mediator.utils.CustomHttpRequest;
import vn.ript.mediator.utils.CustomResponse;
import vn.ript.mediator.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/clients")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientController {

    @Autowired
    OrganizationService organizationService;

    @Autowired
    ServiceDescriptionService serviceDescriptionService;

    @Autowired
    ServiceService serviceService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(name = "exclude_local", required = false) Boolean exclude_local,
            @RequestParam(name = "internal_search", required = false) Boolean internal_search,
            @RequestParam(name = "show_members", required = false) Boolean show_members,
            @RequestParam(name = "instance", required = false) String instance) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            httpRequest.add_query_param("exclude_local", exclude_local);
            httpRequest.add_query_param("internal_search", internal_search);
            httpRequest.add_query_param("show_members", show_members);
            httpRequest.add_query_param("instance", instance);
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

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            if (!body.containsKey("client") ||
                    !body.containsKey("adapter_data")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            Map<String, String> client_tmp = (Map<String, String>) body.get("client");
            if (!client_tmp.containsKey("member_name") ||
                    !client_tmp.containsKey("member_class") ||
                    !client_tmp.containsKey("member_code") ||
                    !client_tmp.containsKey("subsystem_code") ||
                    !client_tmp.containsKey("connection_type")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            Map<String, String> adapter_data_tmp = (Map<String, String>) body.get("adapter_data");
            if (!adapter_data_tmp.containsKey("organId") ||
                    !adapter_data_tmp.containsKey("organName") ||
                    !adapter_data_tmp.containsKey("organAdd") ||
                    !adapter_data_tmp.containsKey("email") ||
                    !adapter_data_tmp.containsKey("telephone") ||
                    !adapter_data_tmp.containsKey("fax") ||
                    !adapter_data_tmp.containsKey("website")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            Boolean ignore_warnings = (Boolean) body.get("ignore_warnings");
            JSONObject client = new JSONObject();
            client.put("member_name", client_tmp.get("member_name"));
            client.put("member_class", client_tmp.get("member_class"));
            client.put("member_code", client_tmp.get("member_code"));
            client.put("subsystem_code", client_tmp.get("subsystem_code"));
            client.put("connection_type", client_tmp.get("connection_type"));
            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("ignore_warnings", ignore_warnings);
            jsonPostObject.put("client", client);
            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String organId = adapter_data_tmp.get("organId");
                String ssId = jsonObject.getString("id");
                String organizationInCharge = null;
                if (adapter_data_tmp.containsKey("organizationInCharge")) {
                    organizationInCharge = adapter_data_tmp.get("organizationInCharge");
                }
                String organName = adapter_data_tmp.get("organName");
                String organAdd = adapter_data_tmp.get("organAdd");
                String email = adapter_data_tmp.get("email");
                String telephone = adapter_data_tmp.get("telephone");
                String fax = adapter_data_tmp.get("fax");
                String website = adapter_data_tmp.get("website");
                String subsystem_code = Utils.SS_MANAGE_ID.replace(':', '/');
                String xRoadClient = Utils.SS_ID.replace(':', '/');
                String urlManage = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                        "/" + Utils.SS_MANAGE_SERVICE_CODE + "/organizations";
                Map<String, String> headersManage = new HashMap<>();
                headersManage.put("X-Road-Client", xRoadClient);

                JSONObject jsonPostObjectManage = new JSONObject();
                jsonPostObjectManage.put("organId", organId);
                jsonPostObjectManage.put("ssId", ssId);
                jsonPostObjectManage.put("organizationInCharge", organizationInCharge);
                jsonPostObjectManage.put("organName", organName);
                jsonPostObjectManage.put("organAdd", organAdd);
                jsonPostObjectManage.put("email", email);
                jsonPostObjectManage.put("telephone", telephone);
                jsonPostObjectManage.put("fax", fax);
                jsonPostObjectManage.put("website", website);

                StringEntity entityManage = new StringEntity(jsonPostObjectManage.toString(),
                        ContentType.APPLICATION_JSON);

                CustomHttpRequest httpRequestManage = new CustomHttpRequest("POST", urlManage, headersManage);
                HttpResponse httpResponseManage = httpRequestManage.request(entityManage);
                if (httpResponseManage.getStatusLine().getStatusCode() == 201) {
                    Organization organization = new Organization();
                    String UUID = Utils.UUID();
                    organization.setId(UUID);
                    organization.setOrganId(organId);
                    organization.setSsId(ssId);
                    organization.setOrganizationInCharge(organizationInCharge);
                    organization.setOrganName(organName);
                    organization.setOrganAdd(organAdd);
                    organization.setEmail(email);
                    organization.setTelephone(telephone);
                    organization.setFax(fax);
                    organization.setWebsite(website);
                    Organization organizationRes = organizationService.save(organization);
                    jsonObject.put("adapter_data", organizationRes);
                    return CustomResponse.Response_data(httpResponseManage.getStatusLine().getStatusCode(),
                            jsonObject.toMap());
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

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                Optional<Organization> checkOrganization = organizationService.findBySsId(jsonObject.getString("id"));
                if (checkOrganization.isPresent()) {
                    Organization organization = checkOrganization.get();
                    jsonObject.put("adapter_data", organization);
                } else {
                    jsonObject.put("adapter_data", new JSONObject());
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonObject.toMap());
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
    public ResponseEntity<Map<String, Object>> editpatchById(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));
            if (!body.containsKey("connection_type") ||
                    !body.containsKey("adapter_data")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            Map<String, String> adapter_data_tmp = (Map<String, String>) body.get("adapter_data");
            if (!adapter_data_tmp.containsKey("organId") ||
                    !adapter_data_tmp.containsKey("organName") ||
                    !adapter_data_tmp.containsKey("organAdd") ||
                    !adapter_data_tmp.containsKey("email") ||
                    !adapter_data_tmp.containsKey("telephone") ||
                    !adapter_data_tmp.containsKey("fax") ||
                    !adapter_data_tmp.containsKey("website")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("connection_type", body.get("connection_type"));

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PATCH", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                Optional<Organization> checkOrganization = organizationService.findBySsId(jsonObject.getString("id"));
                if (checkOrganization.isPresent()) {
                    Organization organization = checkOrganization.get();
                    organization.setOrganId(adapter_data_tmp.get("organId"));
                    String organizationInCharge = null;
                    if (adapter_data_tmp.containsKey("organizationInCharge")) {
                        organizationInCharge = adapter_data_tmp.get("organizationInCharge");
                    }
                    organization.setOrganizationInCharge(organizationInCharge);
                    organization.setOrganName(adapter_data_tmp.get("organName"));
                    organization.setOrganAdd(adapter_data_tmp.get("organAdd"));
                    organization.setEmail(adapter_data_tmp.get("email"));
                    organization.setTelephone(adapter_data_tmp.get("telephone"));
                    organization.setFax(adapter_data_tmp.get("fax"));
                    organization.setWebsite(adapter_data_tmp.get("website"));
                    organizationService.save(organization);
                    jsonObject.put("adapter_data", organization);
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonObject.toMap());
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
            String url = Utils.SS_CONFIG_URL + "/clients/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("DELETE", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                Optional<Organization> checkOrganization = organizationService.findBySsId(id);
                if (checkOrganization.isPresent()) {
                    Organization organization = checkOrganization.get();
                    organizationService.deleteById(organization.getId());
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

    @PutMapping("/{id}/register")
    public ResponseEntity<Map<String, Object>> registerById(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/register";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
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
    public ResponseEntity<Map<String, Object>> getAllServiceClientsOfClient(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/service-clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
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

    @GetMapping("/{client_id}/service-clients/{id}")
    public ResponseEntity<Map<String, Object>> getServiceClientsByServiceClientId(
            @PathVariable String client_id,
            @PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + client_id + "/service-clients/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                Optional<Organization> checkOrganization = organizationService.findBySsId(jsonObject.getString("id"));
                if (checkOrganization.isPresent()) {
                    Organization organization = checkOrganization.get();
                    jsonObject.put("adapter_data", organization);
                } else {
                    jsonObject.put("adapter_data", new JSONObject());
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonObject.toMap());
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{client_id}/service-clients/{id}/access-rights")
    public ResponseEntity<Map<String, Object>> getAllAccessRightOfServiceClientByServiceClientId(
            @PathVariable String client_id,
            @PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + client_id + "/service-clients/" + id + "/access-rights";
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

    @PostMapping("/{client_id}/service-clients/{id}/access-rights")
    public ResponseEntity<Map<String, Object>> addAccessRightOfServiceClientByServiceClientId(
            @PathVariable String client_id,
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("items")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String url = Utils.SS_CONFIG_URL + "/clients/" + client_id + "/service-clients/" + id + "/access-rights";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));

            List<Map<String, String>> items = (List<Map<String, String>>) body.get("items");
            JSONArray jsonPostArray = new JSONArray();
            for (Map<String, String> item : items) {
                String service_code = item.get("service_code");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("service_code", service_code);
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

    @PostMapping("/{client_id}/service-clients/{id}/access-rights/delete")
    public ResponseEntity<Map<String, Object>> deleteAccessRightOfServiceClientByServiceClientId(
            @PathVariable String client_id,
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("items")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String url = Utils.SS_CONFIG_URL + "/clients/" + client_id + "/service-clients/" + id
                    + "/access-rights/delete";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));

            List<Map<String, String>> items = (List<Map<String, String>>) body.get("items");
            JSONArray jsonPostArray = new JSONArray();
            for (Map<String, String> item : items) {
                String service_code = item.get("service_code");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("service_code", service_code);
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

    @GetMapping("/{id}/sign-certificates")
    public ResponseEntity<Map<String, Object>> getAllSignCertificatesByClientId(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/sign-certificates";
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/tls-certificates")
    public ResponseEntity<Map<String, Object>> getAllTLSCertificatesByClientId(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/tls-certificates";
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/tls-certificates")
    public ResponseEntity<Map<String, Object>> addTLSCertificatesByClientId(
            @PathVariable String id,
            @RequestPart(name = "file") MultipartFile file) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/tls-certificates";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/octet-stream"),
                    Map.entry("Accept", "application/json"));
            FileEntity entity = new FileEntity(Utils.MULTIPART_FILE_TO_FILE(file, Constants.LOAI_FILE.CONFIG.ma()),
                    ContentType.APPLICATION_OCTET_STREAM);

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

    @GetMapping("/{id}/tls-certificates/{hash}")
    public ResponseEntity<Map<String, Object>> addTLSCertificateOfClientByHash(
            @PathVariable String id,
            @PathVariable String hash) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/tls-certificates/" + hash;
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
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @DeleteMapping("/{id}/tls-certificates/{hash}")
    public ResponseEntity<Map<String, Object>> deleteTLSCertificateOfClientByHash(
            @PathVariable String id,
            @PathVariable String hash) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/tls-certificates/" + hash;
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

    @PutMapping("/{id}/unregister")
    public ResponseEntity<Map<String, Object>> unregisterById(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/unregister";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
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

    @GetMapping("/{id}/service-descriptions")
    public ResponseEntity<Map<String, Object>> getAllServiceDescriptionOfClient(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/service-descriptions";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                Gson gson = new Gson();

                List<Object> objectServiceDescriptions = new JSONArray(jsonResponse).toList();
                List<ServiceDescription> serviceDescriptions = serviceDescriptionService.findByOrganizationSsId(id);
                JSONArray jsonServiceDescriptions = new JSONArray();
                for (Object objectServiceDescription : objectServiceDescriptions) {
                    JSONObject jsonServiceDescription = new JSONObject(gson.toJson(objectServiceDescription));
                    ServiceDescription currentServiceDescription = null;
                    for (ServiceDescription serviceDescription : serviceDescriptions) {
                        if (serviceDescription.getSsId().equalsIgnoreCase(jsonServiceDescription.getString("id"))) {
                            currentServiceDescription = serviceDescription;
                            break;
                        }
                    }
                    if (currentServiceDescription != null) {
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
                    jsonServiceDescriptions.put(jsonServiceDescription);
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonServiceDescriptions.toList());
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/service-descriptions")
    public ResponseEntity<Map<String, Object>> addServiceDescriptionOfClient(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/service-descriptions";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            if (!body.containsKey("type") ||
                    !body.containsKey("adapter_data") ||
                    !body.containsKey("url")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            JSONObject jsonPostObject = new JSONObject();
            if (!((String) body.get("type")).equalsIgnoreCase("WSDL")) {
                if (!body.containsKey("rest_service_code")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin!");
                }
                jsonPostObject.put("rest_service_code", body.get("rest_service_code"));
            }
            jsonPostObject.put("type", body.get("type"));
            jsonPostObject.put("url", body.get("url"));

            Map<String, Object> adapter_data_tmp = (Map<String, Object>) body.get("adapter_data");
            if (!adapter_data_tmp.containsKey("service_description") ||
                    !adapter_data_tmp.containsKey("service")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            Map<String, String> adapter_data_tmp_service_description = (Map<String, String>) adapter_data_tmp
                    .get("service_description");
            Map<String, String> adapter_data_tmp_service = (Map<String, String>) adapter_data_tmp.get("service");
            if (!adapter_data_tmp_service_description.containsKey("description") ||
                    !adapter_data_tmp_service.containsKey("description") ||
                    !adapter_data_tmp_service.containsKey("isPublic") ||
                    !adapter_data_tmp_service.containsKey("isForCitizen") ||
                    !adapter_data_tmp_service.containsKey("type")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
                Gson gson = new Gson();
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                JSONObject jsonObject = new JSONObject(jsonResponse);
                Optional<Organization> checkOrganization = organizationService
                        .findBySsId(jsonObject.getString("client_id"));
                if (checkOrganization.isPresent()) {
                    ServiceDescription serviceDescription = new ServiceDescription();
                    serviceDescription.setSsId(jsonObject.getString("id"));
                    serviceDescription.setDescription(adapter_data_tmp_service_description.get("description"));
                    serviceDescription.setOrganization(checkOrganization.get());

                    List<Service> services_tmp = new ArrayList<>();
                    if (!((String) body.get("type")).equalsIgnoreCase("WSDL")) {
                        Service service = new Service();
                        String serviceSsId = jsonObject.getString("client_id") + ":"
                                + ((String) body.get("rest_service_code"));
                        service.setSsId(serviceSsId);
                        service.setEndpoints(new ArrayList<>());
                        service.setDescription(adapter_data_tmp_service.get("description"));
                        service.setIsPublic(Boolean.valueOf(adapter_data_tmp_service.get("isPublic")));
                        service.setIsForCitizen(Boolean.valueOf(adapter_data_tmp_service.get("isForCitizen")));
                        service.setType(adapter_data_tmp_service.get("type"));
                        Service serviceRes = serviceService.save(service);
                        services_tmp.add(serviceRes);
                        serviceDescription.setServices(services_tmp);
                        ServiceDescription serviceDescriptionRes = serviceDescriptionService.save(serviceDescription);

                        JSONObject adapter_data_service_description = new JSONObject();
                        adapter_data_service_description.put("description", serviceDescriptionRes.getDescription());
                        jsonObject.put("adapter_data", adapter_data_service_description);

                        List<Object> objectServices = jsonObject.getJSONArray("services").toList();
                        JSONArray jsonServices = new JSONArray();
                        JSONObject jsonService = new JSONObject(gson.toJson(objectServices.get(0)));
                        JSONObject adapter_data_service = new JSONObject();
                        adapter_data_service.put("description", serviceRes.getDescription());
                        jsonService.put("adapter_data", adapter_data_service);
                        jsonServices.put(jsonService);

                        jsonObject.put("services", jsonServices);
                    }
                }
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonObject.toMap());

            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/service-client-candidates")
    public ResponseEntity<Map<String, Object>> getAllServiceClientCandidatesOfClient(
            @PathVariable String id,
            @RequestParam(name = "member_name_group_description", required = false) String member_name_group_description,
            @RequestParam(name = "member_group_code", required = false) String member_group_code,
            @RequestParam(name = "subsystem_code", required = false) String subsystem_code,
            @RequestParam(name = "instance", required = false) String instance,
            @RequestParam(name = "member_class", required = false) String member_class,
            @RequestParam(name = "service_client_type", required = false) String service_client_type) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id +
                    "/service-client-candidates";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            httpRequest.add_query_param("member_name_group_description", member_name_group_description);
            httpRequest.add_query_param("member_group_code", member_group_code);
            httpRequest.add_query_param("subsystem_code", subsystem_code);
            httpRequest.add_query_param("instance", instance);
            httpRequest.add_query_param("member_class", member_class);
            httpRequest.add_query_param("service_client_type", service_client_type);
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

    // @PutMapping("/{id}/make-owner")
    // public ResponseEntity<Map<String, Object>> makeOwner(@PathVariable String id)
    // {
    // try {
    // String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/make-owner";
    // Map<String, String> headers = Map.ofEntries(
    // Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
    // Map.entry("Content-Type", "application/json"),
    // Map.entry("Accept", "application/json"));

    // CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
    // HttpResponse httpResponse = httpRequest.request();
    // if (httpResponse.getStatusLine().getStatusCode() == 204) {
    // return
    // CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
    // } else {
    // return
    // CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
    // httpResponse.toString());
    // }
    // } catch (Exception e) {
    // return CustomResponse.Response_data(500, e.toString());
    // }
    // }

}
