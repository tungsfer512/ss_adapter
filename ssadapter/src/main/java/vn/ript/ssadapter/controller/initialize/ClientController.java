package vn.ript.ssadapter.controller.initialize;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/clients")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll() {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get all clients successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get all clients failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
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

            Map<String, String> client_tmp = (Map<String, String>) body.get("client");
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
                System.out.println("Add new client successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Add new client failed: " + httpResponse);
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
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
                System.out.println("Get client by id successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get client by id failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> patchById(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("connection_type", body.get("connection_type"));

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PATCH", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Patch client by id successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Patch client by id failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
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
                System.out.println("Delete client by id successfully!");
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                System.out.println("Delete client by id failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/register")
    public ResponseEntity<Map<String, Object>> register(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/register";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                System.out.println("Register client successfully!");
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                System.out.println("Register client failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/service-clients")
    public ResponseEntity<Map<String, Object>> getAllServiceClients(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/service-clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get all service clients successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get all service clients failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{client_id}/service-clients/{id}")
    public ResponseEntity<Map<String, Object>> getServiceClientsById(
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
                System.out.println("Get service client by id successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get service client by id failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{client_id}/service-clients/{id}/access-rights")
    public ResponseEntity<Map<String, Object>> getAllAccessRightOfServiceClient(
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
                System.out.println("Get all access right of service client by id successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println(
                        "Get all access right of service client by id failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{client_id}/service-clients/{id}/access-rights")
    public ResponseEntity<Map<String, Object>> addAccessRightOfServiceClient(
            @PathVariable String client_id,
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
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
                System.out.println("Add access right for service client by id successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Add access right for service client by id failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{client_id}/service-clients/{id}/access-rights/delete")
    public ResponseEntity<Map<String, Object>> deleteAccessRightOfServiceClient(
            @PathVariable String client_id,
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
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
                System.out.println("Get all access right id service client by id successfully!");
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                System.out.println(
                        "Get all access right id service client by id failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/sign-certificates")
    public ResponseEntity<Map<String, Object>> getAllSignCertificates(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/sign-certificates";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get all sign-certificates successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get all sign-certificates failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/tls-certificates")
    public ResponseEntity<Map<String, Object>> getAllTLSCertificates(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/tls-certificates";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get all TLS certificates successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get all TLS certificates failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/tls-certificates")
    public ResponseEntity<Map<String, Object>> addTLSCertificates(
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
                System.out.println("Add TLS certificate successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Add TLS certificate failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/tls-certificates/{hash}")
    public ResponseEntity<Map<String, Object>> addTLSCertificateByHash(
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
                System.out.println("Get TLS certificate by hash successfully!");

                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());


                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get TLS certificate by hash failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @DeleteMapping("/{id}/tls-certificates/{hash}")
    public ResponseEntity<Map<String, Object>> deleteTLSCertificateByHash(
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
                System.out.println("Delete TLS certificate by hash successfully!");
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                System.out.println("Delete TLS certificate by hash failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/unregister")
    public ResponseEntity<Map<String, Object>> unregister(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/unregister";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                System.out.println("Register client successfully!");
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                System.out.println("Register client failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/service-descriptions")
    public ResponseEntity<Map<String, Object>> getAllServiceClient(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/service-descriptions";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get initialization status successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get initialization status failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/service-descriptions")
    public ResponseEntity<Map<String, Object>> addServiceClient(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/service-descriptions";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            JSONObject jsonPostObject = new JSONObject();
            if (!((String) body.get("rest_service_code")).equalsIgnoreCase("WSDL")) {
                jsonPostObject.put("rest_service_code", body.get("rest_service_code"));
            }
            jsonPostObject.put("type", body.get("type"));
            jsonPostObject.put("url", body.get("url"));

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
                System.out.println("Get initialization status successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get initialization status failed: " + httpResponse);
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/service-client-candidates")
    public ResponseEntity<Map<String, Object>> getAllServiceClientCandidates(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/service-client-candidates";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                System.out.println("Get all service client candidates successfully!");
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                System.out.println("Get all service client candidates failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/make-owner")
    public ResponseEntity<Map<String, Object>> makeOwner(@PathVariable String id) {
        try {
            String url = Utils.SS_CONFIG_URL + "/clients/" + id + "/make-owner";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                System.out.println("Register client successfully!");
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                System.out.println("Register client failed: " + httpResponse.getStatusLine());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        httpResponse.getStatusLine().toString());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
