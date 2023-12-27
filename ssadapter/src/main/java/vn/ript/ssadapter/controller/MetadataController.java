package vn.ript.ssadapter.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.gson.Gson;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

public class MetadataController {
    // @GetMapping("/listClients")
    // public ResponseEntity<Map<String, Object>> listClients() {
    //     try {
    //         String url = Utils.SS_BASE_URL + "/listClients";
    //         Map<String, String> headers = Map.ofEntries(
    //                 Map.entry("Accept", "application/json"));
    //         CustomHttpRequest customHttpRequest = new CustomHttpRequest("GET", url, headers);
    //         HttpResponse httpResponse = customHttpRequest.request();
    //         if (httpResponse.getStatusLine().getStatusCode() == 200) {
    //             String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
    //             JSONObject jsonObject = new JSONObject(jsonResponse);
    //             List<Object> members = jsonObject.getJSONArray("member").toList();
    //             Gson gson = new Gson();

    //             JSONArray jsonMembers = new JSONArray();
    //             for (Object member : members) {
    //                 JSONObject jsonMember = new JSONObject(gson.toJson(member));
    //                 JSONObject jsonMemberId = jsonMember.getJSONObject("id");
    //                 String memberId = "";
    //                 memberId += jsonMemberId.getString("xroad_instance") + ":";
    //                 memberId += jsonMemberId.getString("member_class") + ":";
    //                 memberId += jsonMemberId.getString("member_code");
    //                 if (jsonMemberId.has("subsystem_code")) {
    //                     memberId += ":" + jsonMemberId.getString("subsystem_code");
    //                 }
    //                 Optional<Organization> checkOrganization = organizationService
    //                         .findBySsId(memberId);
    //                 if (checkOrganization.isPresent()) {
    //                     Organization organization = checkOrganization.get();
    //                     jsonMember.put("adapter_data", organization);
    //                 } else {
    //                     jsonMember.put("adapter_data", new JSONObject());
    //                 }
    //                 jsonMembers.put(jsonMember);
    //             }

    //             return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(), jsonMembers.toList());
    //         } else {
    //             return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
    //                     httpResponse.getStatusLine());
    //         }
    //     } catch (Exception e) {
    //         return CustomResponse.Response_data(500, e);
    //     }
    // }
}
