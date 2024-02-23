package vn.ript.ssadapter.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/logs/clientproxy-access")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientproxyAccessController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll() {
        try {
            String command = "lxc exec " + Utils.SS_CONTAINER_NAME
                    + " -- sh -c \"cat /var/log/xroad/clientproxy_access.log\"";
            List<String> commands = Arrays.asList("sshpass", "-p", Utils.LOCAL_PASSWORD, "ssh", "-o",
                    "StrictHostKeyChecking=no", Utils.LOCAL_USERNAME + "@" + Utils.LOCAL_IP, command);
            System.out.println(commands.toString());
            String jsonResponse = Utils.EXEC_SHELL_COMMAND(commands);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String dataStr = jsonObject.getString("data");
            String[] listData = dataStr.split("\n");
            System.out.println(listData);
            for (String te : listData) {
                System.out.println(te);
                System.out.println("-------------------");
            }
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                return CustomResponse.Response_data(200, dataStr);
            } else {
                return CustomResponse.Response_data(400, dataStr);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
