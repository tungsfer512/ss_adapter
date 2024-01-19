package vn.ript.ssmanageadapter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssmanageadapter.model.AccessRequest;
import vn.ript.ssmanageadapter.model.Organization;
import vn.ript.ssmanageadapter.service.AccessRequestService;
import vn.ript.ssmanageadapter.service.OrganizationService;
import vn.ript.ssmanageadapter.utils.Constants;
import vn.ript.ssmanageadapter.utils.CustomHttpRequest;
import vn.ript.ssmanageadapter.utils.CustomResponse;
import vn.ript.ssmanageadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/access-requests")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccessRequestController {

    @Autowired
    AccessRequestService accessRequestService;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> add(
            @RequestBody Map<String, Object> body) {
        try {

            String toId = null;
            Integer externalServiceId = null;
            Integer externalEndpointId = null;
            String externalId = Utils.UUID();

            if (!body.containsKey("description") ||
                    !body.containsKey("handleType") ||
                    !body.containsKey("fromName") ||
                    !body.containsKey("type")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }
            String fromId = Utils.SS_ID;
            String fromName = (String) body.get("fromName");
            String handleType = (String) body.get("handleType");
            if (handleType.equalsIgnoreCase(Constants.LOAI_XU_LY_YEU_CAU_CAP_QUYEN.MANAGE.ma())) {
                toId = Utils.SS_MANAGE_ID;
            } else {
                toId = (String) body.get("toId");
            }
            String description = (String) body.get("description");
            String type = (String) body.get("type");
            if (type.equalsIgnoreCase(Constants.LOAI_YEU_CAU_CAP_QUYEN.SERVICE.ma())) {
                if (!body.containsKey("externalServiceId")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin");
                }
                externalServiceId = Integer.parseInt((String) body.get("externalServiceId"));
            } else if (type.equalsIgnoreCase(Constants.LOAI_YEU_CAU_CAP_QUYEN.ENDPOINT.ma())) {
                if (!body.containsKey("externalEndpointId")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin");
                }
                externalEndpointId = Integer.parseInt((String) body.get("externalEndpointId"));
            }

            Optional<Organization> checkFrom = organizationService.findBySsId(fromId);
            Optional<Organization> checkTo = organizationService.findBySsId(toId);
            if (!checkFrom.isPresent() || !checkTo.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }

            String subsystem_code = toId.replace(':', '/');
            String xRoadClient = fromId.replace(':', '/');
            String url = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                    "/" + Utils.SS_MANAGE_SERVICE_CODE + "/access-requests";
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Road-Client", xRoadClient);

            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("fromId", fromId);
            jsonPostObject.put("fromName", fromName);
            jsonPostObject.put("toId", toId);
            jsonPostObject.put("externalId", externalId);
            jsonPostObject.put("type", type);
            jsonPostObject.put("externalServiceId", externalServiceId);
            jsonPostObject.put("description", description);
            jsonPostObject.put("externalEndpointId", externalEndpointId);

            System.out.println(url);
            System.out.println(jsonPostObject.toString());

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 201) {
                AccessRequest accessRequest = new AccessRequest();
                accessRequest.setFrom(checkFrom.get());
                accessRequest.setFromName(fromName);
                accessRequest.setTo(checkTo.get());
                accessRequest.setExternalId(externalId);
                accessRequest.setType(type);
                accessRequest.setExternalServiceId(externalServiceId);
                accessRequest.setExternalEndpointId(externalEndpointId);
                accessRequest.setDescription(description);
                accessRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_CAP_QUYEN.PENDING.ma());
                accessRequest.setCreateAt(Utils.DATETIME_NOW());
                accessRequest.setUpdateAt(Utils.DATETIME_NOW());
                accessRequest.setDeclinedReason(null);

                AccessRequest accessRequestRes = accessRequestService.saveAccessRequest(accessRequest);
                SimpleMailMessage mailMessage = new SimpleMailMessage();

                mailMessage.setFrom(sender);
                mailMessage.setSubject("YÊU CẦU XIN CẤP QUYỂN MỚI");
                mailMessage.setTo(accessRequest.getTo().getEmail());
                mailMessage.setText("Có yêu cầu xin cấp quyền mới!");

                javaMailSender.send(mailMessage);
                return CustomResponse.Response_data(201, accessRequestRes);
            } else {
                String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
                return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                        jsonResponse);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<Map<String, Object>> getSentList(
            @RequestParam(name = "toId", required = false) String toId,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "sortCreateAt", required = false) String sortCreateAt) {
        try {
            List<AccessRequest> accessRequests = accessRequestService
                    .findSentWithConditions(toId, type, status, sortCreateAt);
            return CustomResponse.Response_data(200, accessRequests);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/received")
    public ResponseEntity<Map<String, Object>> getReceivedList(
            @RequestParam(name = "fromId", required = false) String fromId,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "sortCreateAt", required = false) String sortCreateAt) {
        try {
            List<AccessRequest> accessRequests = accessRequestService
                    .findReceivedWithConditions(fromId, type, status, sortCreateAt);
            return CustomResponse.Response_data(200, accessRequests);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Integer id) {
        try {
            Optional<AccessRequest> checkAccessRequest = accessRequestService.findById(id);
            if (!checkAccessRequest.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            return CustomResponse.Response_data(200, checkAccessRequest.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approve(@PathVariable Integer id) {
        try {
            Optional<AccessRequest> checkAccessRequest = accessRequestService.findById(id);
            if (!checkAccessRequest.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            AccessRequest accessRequest = checkAccessRequest.get();
            if (accessRequest.getType().equalsIgnoreCase(Constants.LOAI_YEU_CAU_CAP_QUYEN.SERVICE.ma())) {
                String url = Utils.SS_CONFIG_URL + "/services/" + accessRequest.getService().getSsId()
                        + "/service-clients";
                Map<String, String> headers = Map.ofEntries(
                        Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                        Map.entry("Content-Type", "application/json"),
                        Map.entry("Accept", "application/json"));

                JSONArray jsonPostArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", accessRequest.getFrom().getSsId());
                jsonObject.put("name", accessRequest.getFromName());
                jsonObject.put("service_client_type", "SUBSYSTEM");
                jsonPostArray.put(jsonObject);
                JSONObject jsonPostObject = new JSONObject();
                jsonPostObject.put("items", jsonPostArray);
                StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

                CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
                HttpResponse httpResponse = httpRequest.request(entity);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    String subsystem_code = accessRequest.getFrom().getSsId().replace(':', '/');
                    String xRoadClient = accessRequest.getTo().getSsId().replace(':', '/');
                    String urlApprove = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                            "/" + Utils.SS_MANAGE_SERVICE_CODE + "/access-requests/" + accessRequest.getExternalId()
                            + "/approve";
                    Map<String, String> headersApprove = new HashMap<>();
                    headersApprove.put("X-Road-Client", xRoadClient);

                    CustomHttpRequest httpRequestApprove = new CustomHttpRequest("PUT", urlApprove, headersApprove);
                    HttpResponse httpResponseApprove = httpRequestApprove.request();
                    if (httpResponseApprove.getStatusLine().getStatusCode() == 204) {
                        accessRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_CAP_QUYEN.APPROVED.ma());
                        accessRequest.setUpdateAt(Utils.DATETIME_NOW());
                        accessRequestService.saveAccessRequest(accessRequest);
                        return CustomResponse.Response_no_data(204);
                    } else {
                        String jsonResponseApprove = EntityUtils.toString(httpResponseApprove.getEntity());
                        return CustomResponse.Response_data(httpResponseApprove.getStatusLine().getStatusCode(),
                                jsonResponseApprove);
                    }
                } else {
                    return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.toString());
                }
            } else if (accessRequest.getType().equalsIgnoreCase(Constants.LOAI_YEU_CAU_CAP_QUYEN.ENDPOINT.ma())) {
                System.out.println(accessRequest);
                System.out.println(accessRequest.getEndpoint());
                String url = Utils.SS_CONFIG_URL + "/endpoints/" + accessRequest.getEndpoint().getSsId()
                        + "/service-clients";
                Map<String, String> headers = Map.ofEntries(
                        Map.entry("Authorization", "X-Road-ApiKey token=" + Utils.SS_API_KEY),
                        Map.entry("Content-Type", "application/json"),
                        Map.entry("Accept", "application/json"));

                JSONArray jsonPostArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", accessRequest.getFrom().getSsId());
                jsonObject.put("name", accessRequest.getFromName());
                jsonObject.put("service_client_type", "SUBSYSTEM");
                jsonPostArray.put(jsonObject);
                JSONObject jsonPostObject = new JSONObject();
                jsonPostObject.put("items", jsonPostArray);
                StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

                CustomHttpRequest httpRequest = new CustomHttpRequest("POST", url, headers);
                HttpResponse httpResponse = httpRequest.request(entity);
                if (httpResponse.getStatusLine().getStatusCode() == 201) {
                    String subsystem_code = accessRequest.getFrom().getSsId().replace(':', '/');
                    String xRoadClient = accessRequest.getTo().getSsId().replace(':', '/');
                    String urlApprove = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                            "/" + Utils.SS_MANAGE_SERVICE_CODE + "/access-requests/" + accessRequest.getExternalId()
                            + "/approve";
                    Map<String, String> headersApprove = new HashMap<>();
                    headersApprove.put("X-Road-Client", xRoadClient);

                    CustomHttpRequest httpRequestApprove = new CustomHttpRequest("PUT", urlApprove, headersApprove);
                    HttpResponse httpResponseApprove = httpRequestApprove.request();
                    if (httpResponseApprove.getStatusLine().getStatusCode() == 204) {
                        accessRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_CAP_QUYEN.APPROVED.ma());
                        accessRequest.setUpdateAt(Utils.DATETIME_NOW());
                        accessRequestService.saveAccessRequest(accessRequest);
                        return CustomResponse.Response_no_data(204);
                    } else {
                        String jsonResponseApprove = EntityUtils.toString(httpResponseApprove.getEntity());
                        return CustomResponse.Response_data(httpResponseApprove.getStatusLine().getStatusCode(),
                                jsonResponseApprove);
                    }
                } else {
                    return CustomResponse.Response_data(httpResponse.getStatusLine().getStatusCode(),
                            httpResponse.toString());
                }
            } else {
                return CustomResponse.Response_no_data(500);
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/decline")
    public ResponseEntity<Map<String, Object>> add(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        try {
            String declinedReason = null;
            if (body.containsKey("declinedReason")) {
                declinedReason = (String) body.get("declinedReason");
            }
            Optional<AccessRequest> checkAccessRequest = accessRequestService.findById(id);
            if (!checkAccessRequest.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            AccessRequest accessRequest = checkAccessRequest.get();
            String subsystem_code = accessRequest.getFrom().getSsId().replace(':', '/');
            String xRoadClient = accessRequest.getTo().getSsId().replace(':', '/');
            String url = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                    "/" + Utils.SS_MANAGE_SERVICE_CODE + "/access-requests/" + accessRequest.getExternalId()
                    + "/decline";
            Map<String, String> headers = new HashMap<>();
            headers.put("X-Road-Client", xRoadClient);
            JSONObject jsonPostObject = new JSONObject();
            jsonPostObject.put("declinedReason", declinedReason);

            System.out.println(url);
            System.out.println(jsonPostObject.toString());

            StringEntity entity = new StringEntity(jsonPostObject.toString(), ContentType.APPLICATION_JSON);

            CustomHttpRequest httpRequest = new CustomHttpRequest("PUT", url, headers);
            HttpResponse httpResponse = httpRequest.request(entity);
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                accessRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_CAP_QUYEN.DECLINED.ma());
                accessRequest.setUpdateAt(Utils.DATETIME_NOW());
                accessRequest.setDeclinedReason(declinedReason);
                accessRequestService.saveAccessRequest(accessRequest);
                return CustomResponse.Response_no_data(204);
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
