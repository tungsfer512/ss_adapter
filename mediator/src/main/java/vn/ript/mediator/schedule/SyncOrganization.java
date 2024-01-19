package vn.ript.mediator.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import vn.ript.mediator.model.Organization;
import vn.ript.mediator.service.OrganizationService;
import vn.ript.mediator.utils.CustomHttpRequest;
import vn.ript.mediator.utils.Utils;

@Component
public class SyncOrganization {

    @Autowired
    OrganizationService organizationService;

    @Scheduled(fixedDelay = 1000 * 30)
    public void SyncOrganizationEvery30Seconds() {
        // try {
        //     String url = Utils.SS_BASE_URL + "/listClients";
        //     Map<String, String> headers = Map.ofEntries(
        //             Map.entry("Accept", "application/json"));
        //     CustomHttpRequest customHttpRequest = new CustomHttpRequest("GET", url, headers);
        //     HttpResponse httpResponse = customHttpRequest.request();
        //     if (httpResponse.getStatusLine().getStatusCode() == 200) {
        //         String subsystem_code = Utils.SS_MANAGE_ID.replace(":", "/");
        //         String xRoadClient = Utils.SS_ID.replace(":", "/");
        //         String urlManage = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
        //                 "/" + Utils.SS_MANAGE_SERVICE_CODE + "/organizations";
        //         Map<String, String> headersManage = new HashMap<>();
        //         headersManage.put("X-Road-Client", xRoadClient);
        //         CustomHttpRequest customHttpRequestManage = new CustomHttpRequest("GET", urlManage, headersManage);
        //         HttpResponse httpResponseManage = customHttpRequestManage.request();
        //         if (httpResponseManage.getStatusLine().getStatusCode() == 200) {
        //             Gson gson = new Gson();

        //             String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        //             JSONObject jsonObject = new JSONObject(jsonResponse);
        //             List<Object> members = jsonObject.getJSONArray("member").toList();

        //             String jsonResponseManage = EntityUtils.toString(httpResponseManage.getEntity());
        //             JSONObject jsonObjectManage = new JSONObject(jsonResponseManage);
        //             List<Object> membersManage = jsonObjectManage.getJSONArray("data").toList();

        //             List<String> memberManageIdList = new ArrayList<>();
        //             Map<String, Integer> memberManageIdMap = new HashMap<>();
        //             for (int i = 0; i < membersManage.size(); i++) {
        //                 JSONObject jsonMemberManage = new JSONObject(gson.toJson(membersManage.get(i)));
        //                 String ssid_tmp = jsonMemberManage.getString("ssId");
        //                 memberManageIdList.add(ssid_tmp);
        //                 memberManageIdMap.put(ssid_tmp, i);
        //             }

        //             for (Object member : members) {
        //                 JSONObject jsonMember = new JSONObject(gson.toJson(member));
        //                 JSONObject jsonMemberId = jsonMember.getJSONObject("id");
        //                 String memberId = "";
        //                 memberId += jsonMemberId.getString("xroad_instance") + ":";
        //                 memberId += jsonMemberId.getString("member_class") + ":";
        //                 memberId += jsonMemberId.getString("member_code");
        //                 if (jsonMemberId.has("subsystem_code")) {
        //                     memberId += ":" + jsonMemberId.getString("subsystem_code");
        //                     if (memberManageIdList.contains(memberId)) {
        //                         Object objDataOrganization = membersManage.get(memberManageIdMap.get(memberId));
        //                         JSONObject jsonDataOrganization = new JSONObject(gson.toJson(objDataOrganization));

        //                         String organId = jsonDataOrganization.getString("organId");
        //                         String ssId = jsonDataOrganization.getString("ssId");
        //                         String organizationInCharge = jsonDataOrganization.getString("organizationInCharge");
        //                         String organName = jsonDataOrganization.getString("organName");
        //                         String organAdd = jsonDataOrganization.getString("organAdd");
        //                         String email = jsonDataOrganization.getString("email");
        //                         String telephone = jsonDataOrganization.getString("telephone");
        //                         String fax = jsonDataOrganization.getString("fax");
        //                         String website = jsonDataOrganization.getString("website");

        //                         Optional<Organization> checkOrganization = organizationService.findBySsId(memberId);
        //                         if (!checkOrganization.isPresent()) {
        //                             Organization organization = new Organization();
        //                             organization.setOrganId(organId);
        //                             organization.setSsId(ssId);
        //                             organization.setOrganizationInCharge(organizationInCharge);
        //                             organization.setOrganName(organName);
        //                             organization.setOrganAdd(organAdd);
        //                             organization.setEmail(email);
        //                             organization.setTelephone(telephone);
        //                             organization.setFax(fax);
        //                             organization.setWebsite(website);
        //                             organizationService.save(organization);
        //                         } else {
        //                             Organization organization = checkOrganization.get();
        //                             organization.setOrganId(organId);
        //                             organization.setOrganizationInCharge(organizationInCharge);
        //                             organization.setOrganName(organName);
        //                             organization.setOrganAdd(organAdd);
        //                             organization.setEmail(email);
        //                             organization.setTelephone(telephone);
        //                             organization.setFax(fax);
        //                             organization.setWebsite(website);
        //                             organizationService.save(organization);
        //                         }
        //                         System.out.println(
        //                                 "=============================Sync Organization=============================");
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // } catch (ParseException e) {
        //     e.printStackTrace();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}
