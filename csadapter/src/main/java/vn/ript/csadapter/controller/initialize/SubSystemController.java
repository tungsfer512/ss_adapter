package vn.ript.csadapter.controller.initialize;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.csadapter.utils.CustomHttpRequest;
import vn.ript.csadapter.utils.CustomResponse;
import vn.ript.csadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/subsystems")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SubSystemController {

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("subsystem_id")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            String url = Utils.CS_CONFIG_URL + "/subsystems";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.CS_API_KEY),
                    Map.entry("Content-Type", "application/json"),
                    Map.entry("Accept", "application/json"));

            Map<String, String> subsystem_id = (Map<String, String>) body.get("subsystem_id");
            if (!body.containsKey("member_class") ||
                    !body.containsKey("member_code") ||
                    !body.containsKey("subsystem_code")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }

            String member_class = subsystem_id.get("member_class");
            String member_code = subsystem_id.get("member_code");
            String subsystem_code = subsystem_id.get("subsystem_code");

            JSONObject jsonSubSystemId = new JSONObject();
            jsonSubSystemId.put("member_class", member_class);
            jsonSubSystemId.put("member_code", member_code);
            jsonSubSystemId.put("subsystem_code", subsystem_code);

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("subsystem_id", jsonSubSystemId);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);
            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable String id) {
        try {
            String url = Utils.CS_CONFIG_URL + "/subsystems/" + id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.CS_API_KEY));
            CustomHttpRequest httpRequest = new CustomHttpRequest("DELETE", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @DeleteMapping("/{id}/servers/{server_id}")
    public ResponseEntity<Map<String, Object>> deleteServerOfSubS(
            @PathVariable String id,
            @PathVariable String server_id) {
        try {
            String url = Utils.CS_CONFIG_URL + "/subsystems/" + id + "/servers/" + server_id;
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.CS_API_KEY));
            CustomHttpRequest httpRequest = new CustomHttpRequest("DELETE", url, headers);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                return CustomResponse.Response_no_data(httpResponse.getStatusLine().getStatusCode());
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
