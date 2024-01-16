package vn.ript.ssadapter.controller.initialize;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/api-keys")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class APIKeyController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll() {
        try {
            String url = "https://localhost:4000/api/v1/api-keys";
            String command = "lxc exec " + Utils.SS_CONTAINER_NAME + " -- sh -c \"curl -X GET -u "
                    + Utils.SS_ADMIN_USERNAME + ":" + Utils.SS_ADMIN_PASSWORD + " " + url + " -k\"";
            List<String> commands = Arrays.asList("sshpass", "-p", Utils.LOCAL_PASSWORD, "ssh", "-o",
                    "StrictHostKeyChecking=no", Utils.LOCAL_USERNAME + "@" + Utils.LOCAL_IP, command);
            String jsonResponse = Utils.EXEC_SHELL_COMMAND(commands);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String dataStr = jsonObject.getString("data");
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                return CustomResponse.Response_data(200, dataStr);
            } else {
                return CustomResponse.Response_data(400, dataStr);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("roles")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String url = "https://localhost:4000/api/v1/api-keys";
            // String data =
            // "\'[\\\"XROAD_SECURITYSERVER_OBSERVER\\\",\\\"XROAD_REGISTRATION_OFFICER\\\",\\\"XROAD_SERVICE_ADMINISTRATOR\\\",\\\"XROAD_SECURITY_OFFICER\\\",\\\"XROAD_SYSTEM_ADMINISTRATOR\\\"]\'";
            String data = "\'[";
            Gson gson = new Gson();
            System.out.println(body.get("roles"));
            List<Object> objectRoles = new JSONArray(gson.toJson(body.get("roles"))).toList();
            for (Object objectRole : objectRoles) {
                String role = objectRole.toString();
                data += "\\\"" + role.toUpperCase() + "\\\",";
            }
            data = data.substring(0, data.length() - 1);
            data += "]\'";
            String header = "\\\"Content-Type: application/json\\\"";
            String command = "lxc exec " + Utils.SS_CONTAINER_NAME + " -- sh -c \"curl -X POST -u "
                    + Utils.SS_ADMIN_USERNAME + ":" + Utils.SS_ADMIN_PASSWORD + " " + url + " --data " + data
                    + " --header " + header + " -k\"";
            List<String> commands = Arrays.asList("sshpass", "-p", Utils.LOCAL_PASSWORD, "ssh", "-o",
                    "StrictHostKeyChecking=no", Utils.LOCAL_USERNAME + "@" + Utils.LOCAL_IP, command);
            String jsonResponse = Utils.EXEC_SHELL_COMMAND(commands);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String dataStr = jsonObject.getString("data");
            System.out.println(dataStr);
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                return CustomResponse.Response_data(200, dataStr);
            } else {
                return CustomResponse.Response_data(400, dataStr);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
        try {
            String url = "https://localhost:4000/api/v1/api-keys/" + id;
            String command = "lxc exec " + Utils.SS_CONTAINER_NAME + " -- sh -c \"curl -X GET -u "
                    + Utils.SS_ADMIN_USERNAME + ":" + Utils.SS_ADMIN_PASSWORD + " " + url + " -k\"";
            List<String> commands = Arrays.asList("sshpass", "-p", Utils.LOCAL_PASSWORD, "ssh", "-o",
                    "StrictHostKeyChecking=no", Utils.LOCAL_USERNAME + "@" + Utils.LOCAL_IP, command);
            String jsonResponse = Utils.EXEC_SHELL_COMMAND(commands);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String dataStr = jsonObject.getString("data");
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                return CustomResponse.Response_data(200, dataStr);
            } else {
                return CustomResponse.Response_data(400, dataStr);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> putById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("roles")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            String url = "https://localhost:4000/api/v1/api-keys/" + id;
            // String data =
            // "\'[\\\"XROAD_SECURITYSERVER_OBSERVER\\\",\\\"XROAD_REGISTRATION_OFFICER\\\",\\\"XROAD_SERVICE_ADMINISTRATOR\\\",\\\"XROAD_SECURITY_OFFICER\\\",\\\"XROAD_SYSTEM_ADMINISTRATOR\\\"]\'";
            String data = "\'[";
            Gson gson = new Gson();
            List<Object> objectRoles = new JSONArray(gson.toJson(body.get("roles"))).toList();
            for (Object objectRole : objectRoles) {
                String role = objectRole.toString();
                data += "\\\"" + role.toUpperCase() + "\\\",";
            }
            data = data.substring(0, data.length() - 1);
            data += "]\'";
            String header = "\\\"Content-Type: application/json\\\"";
            String command = "lxc exec " + Utils.SS_CONTAINER_NAME + " -- sh -c \"curl -X PUT -u "
                    + Utils.SS_ADMIN_USERNAME + ":" + Utils.SS_ADMIN_PASSWORD + " " + url + " --data " + data
                    + " --header " + header + " -k\"";
            List<String> commands = Arrays.asList("sshpass", "-p", Utils.LOCAL_PASSWORD, "ssh", "-o",
                    "StrictHostKeyChecking=no", Utils.LOCAL_USERNAME + "@" + Utils.LOCAL_IP, command);
            String jsonResponse = Utils.EXEC_SHELL_COMMAND(commands);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                return CustomResponse.Response_data(200, jsonObject.getString("data"));
            } else {
                return CustomResponse.Response_data(400, jsonObject.getString("data"));
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteById(@PathVariable Integer id) {
        try {
            String url = "https://localhost:4000/api/v1/api-keys/" + id;
            String command = "lxc exec " + Utils.SS_CONTAINER_NAME + " -- sh -c \"curl -X DELETE -u "
                    + Utils.SS_ADMIN_USERNAME + ":" + Utils.SS_ADMIN_PASSWORD + " " + url + " -k\"";
            List<String> commands = Arrays.asList("sshpass", "-p", Utils.LOCAL_PASSWORD, "ssh", "-o",
                    "StrictHostKeyChecking=no", Utils.LOCAL_USERNAME + "@" + Utils.LOCAL_IP, command);
            String jsonResponse = Utils.EXEC_SHELL_COMMAND(commands);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                return CustomResponse.Response_no_data(204);
            } else {
                return CustomResponse.Response_data(400, jsonObject.getString("data"));
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
