package vn.ript.csadapter.controller.initialize;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.csadapter.utils.CustomHttpRequest;
import vn.ript.csadapter.utils.CustomResponse;
import vn.ript.csadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/initialize/clients")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientController {

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "desc", required = false) String desc,
            @RequestParam(name = "limit", required = false) String limit,
            @RequestParam(name = "offset", required = false) String offset,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "instance", required = false) String instance,
            @RequestParam(name = "member_class", required = false) String member_class,
            @RequestParam(name = "member_code", required = false) String member_code,
            @RequestParam(name = "subsystem_code", required = false) String subsystem_code,
            @RequestParam(name = "client_type", required = false) String client_type,
            @RequestParam(name = "security_server", required = false) String security_server,
            @RequestParam(name = "excluding_group", required = false) String excluding_group) {
        try {
            String url = Utils.CS_CONFIG_URL + "/clients";
            Map<String, String> headers = Map.ofEntries(
                    Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.CS_API_KEY),
                    Map.entry("Accept", "application/json"));
            CustomHttpRequest httpRequest = new CustomHttpRequest("GET", url, headers);
            httpRequest.add_query_param("q", q);
            httpRequest.add_query_param("sort", sort);
            httpRequest.add_query_param("desc", desc);
            httpRequest.add_query_param("limit", limit);
            httpRequest.add_query_param("offset", offset);
            httpRequest.add_query_param("name", name);
            httpRequest.add_query_param("instance", instance);
            httpRequest.add_query_param("member_class", member_class);
            httpRequest.add_query_param("member_code", member_code);
            httpRequest.add_query_param("subsystem_code", subsystem_code);
            httpRequest.add_query_param("client_type", client_type);
            httpRequest.add_query_param("security_server", security_server);
            httpRequest.add_query_param("excluding_group", excluding_group);
            HttpResponse httpResponse = httpRequest.request();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
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

}
