package vn.ript.ssadapter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/v1/organizations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrganizationController {
    @Autowired
    OrganizationService organizationService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getOrganizationsList() {
        try {
            List<Organization> organizations = organizationService.findAllExcept(Utils.SS_ID);
            return CustomResponse.Response_data(200, organizations);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/{ssId}")
    public ResponseEntity<Map<String, Object>> getOrganizationsBySsId(@PathVariable String ssId) {
        try {
            Optional<Organization> checkOrganization = organizationService.findBySsId(ssId);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            return CustomResponse.Response_data(200, checkOrganization.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> registerOrganization(
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("organId") ||
                    !body.containsKey("organName") ||
                    !body.containsKey("organAdd") ||
                    !body.containsKey("email") ||
                    !body.containsKey("telephone") ||
                    !body.containsKey("fax") ||
                    !body.containsKey("website")) {
                return CustomResponse.Response_data(400, "Thieu thong tin!");
            }
            Organization organization = new Organization();
            String organId = body.get("organId").toString();
            String ssId = body.get("ssId").toString();
            String organizationInCharge = null;
            if (body.containsKey("organizationInCharge")) {
                organizationInCharge = (String) body.get("organizationInCharge");
            }
            String organName = body.get("organName").toString();
            String organAdd = body.get("organAdd").toString();
            String email = body.get("email").toString();
            String telephone = body.get("telephone").toString();
            String fax = body.get("fax").toString();
            String website = body.get("website").toString();

            String subsystem_code = Utils.SS_MANAGE_ID.replace(':', '/');
            String xRoadClient = Utils.SS_ID.replace(':', '/');
            String url = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                    "/" + Utils.SS_MANAGE_SERVICE_CODE + "/organizations";
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Road-Client", xRoadClient);

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("organId", organId);
            jsonPostObject.put("ssId", ssId);
            jsonPostObject.put("organizationInCharge", organizationInCharge);
            jsonPostObject.put("organName", organName);
            jsonPostObject.put("organAdd", organAdd);
            jsonPostObject.put("email", email);
            jsonPostObject.put("telephone", telephone);
            jsonPostObject.put("fax", fax);
            jsonPostObject.put("website", website);

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {

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
                return CustomResponse.Response_data(201, organizationRes);
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    // @DeleteMapping("/")
    // public ResponseEntity<Map<String, Object>> deleteOrganization(
    // @RequestHeader(name = "OrganizationCode", required = true) String
    // organizationCode) {
    // try {
    // if (organizationCode == null) {
    // return Response.Response_500();
    // } else {
    // organizationService.deleteOrganizationByCode(organizationCode);
    // return Response.Response_200();
    // }
    // } catch (Exception e) {
    // return Response.Response_500(e);
    // }
    // }
}
