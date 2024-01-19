package vn.ript.mediator.controller.initialize;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.ript.mediator.utils.Constants;
import vn.ript.mediator.utils.CustomHttpRequest;
import vn.ript.mediator.utils.CustomResponse;
import vn.ript.mediator.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/system")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SystemController {

    @GetMapping("/anchor")
    public ResponseEntity<Map<String, Object>> getAnchor() {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/anchor";
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

    @PostMapping("/anchor")
    public ResponseEntity<Map<String, Object>> uploadAnchor(
            @RequestPart(name = "file", required = true) MultipartFile file) {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/anchor";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/octet-stream"));
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

    @PutMapping("/anchor")
    public ResponseEntity<Map<String, Object>> editAnchor(
            @RequestPart(name = "file", required = true) MultipartFile file) {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/anchor";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/octet-stream"));
            FileEntity entity = new FileEntity(Utils.MULTIPART_FILE_TO_FILE(file, Constants.LOAI_FILE.CONFIG.ma()),
                    ContentType.APPLICATION_OCTET_STREAM);
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

    @GetMapping("/anchor/download")
    public ResponseEntity<InputStreamResource> downloadAnchor() {
        try {
            String filename = Utils.UUID() + ".xml";
            String url = Utils.SS_CONFIG_URL + "/system/anchor/download";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY));
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

    @GetMapping("/certificate")
    public ResponseEntity<Map<String, Object>> getTLSCertificate() {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/certificate";
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

    @PostMapping("/certificate")
    public ResponseEntity<Map<String, Object>> generateTLSCertificate() {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/certificate";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
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

    @PostMapping("/certificate/import")
    public ResponseEntity<Map<String, Object>> importTLSCertificate(
            @RequestPart(name = "file", required = true) MultipartFile file) {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/certificate/import";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
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

    @GetMapping("/certificate/export")
    public ResponseEntity<InputStreamResource> exportTLSCertificate() {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/certificate/export";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Accept", "application/gzip"));
            String filename = "cert.tar.gz";
            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
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

    @GetMapping("/timestamping-services")
    public ResponseEntity<Map<String, Object>> getAllTSA() {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/timestamping-services";
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
            // return CustomResponse.Response_no_data(200);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/timestamping-services")
    public ResponseEntity<Map<String, Object>> addTSA(
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/timestamping-services";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            if (!body.containsKey("name") ||
                    !body.containsKey("url")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String name = (String) body.get("name");
            String tsa_url = (String) body.get("url");

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("name", name);
            jsonPostObject.put("url", tsa_url);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);
            System.out.println(jsonPostObject);
            System.out.println(jsonPostObject.toString());
            System.out.println(entity);
            System.out.println(entity.toString());

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

    @PostMapping("/timestamping-services/delete")
    public ResponseEntity<Map<String, Object>> deleteTSA(
            @RequestBody Map<String, Object> body) {
        try {
            String url = Utils.SS_CONFIG_URL + "/system/timestamping-services/delete";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));
            if (!body.containsKey("name") ||
                    !body.containsKey("url")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String name = (String) body.get("name");
            String tsa_url = (String) body.get("url");

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("name", name);
            jsonPostObject.put("url", tsa_url);

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
