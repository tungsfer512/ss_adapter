package vn.ript.mediator.controller.apilienthong;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.mediator.model.AccessRequest;
import vn.ript.mediator.model.Organization;
import vn.ript.mediator.model.initialize.Endpoint;
import vn.ript.mediator.model.initialize.Service;
import vn.ript.mediator.service.AccessRequestService;
import vn.ript.mediator.service.OrganizationService;
import vn.ript.mediator.service.initialize.EndpointService;
import vn.ript.mediator.service.initialize.ServiceService;
import vn.ript.mediator.utils.Constants;
import vn.ript.mediator.utils.CustomResponse;
import vn.ript.mediator.utils.Utils;

@RestController
@RequestMapping("api/lienthong/v1/access-requests")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccessRequestLienThongController {

    @Autowired
    AccessRequestService accessRequestService;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    ServiceService serviceService;
    @Autowired
    EndpointService endpointService;

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> add(
            @RequestBody Map<String, Object> body) {
        try {

            Integer serviceId = null;
            Integer endpointId = null;
            Service service = null;
            Endpoint endpoint = null;

            if (!body.containsKey("fromId") ||
                    !body.containsKey("fromName") ||
                    !body.containsKey("toId") ||
                    !body.containsKey("externalId") ||
                    !body.containsKey("description") ||
                    !body.containsKey("type")) {
                return CustomResponse.Response_data(400, "Thieu thong tin");
            }
            String fromId = (String) body.get("fromId");
            String fromName = (String) body.get("fromName");
            String toId = (String) body.get("toId");
            String externalId = (String) body.get("externalId");
            String description = (String) body.get("description");
            String type = (String) body.get("type");
            if (type.equalsIgnoreCase(Constants.LOAI_YEU_CAU_CAP_QUYEN.SERVICE.ma())) {
                if (!body.containsKey("externalServiceId")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin");
                }
                serviceId = Integer.parseInt(body.get("externalServiceId").toString());
                Optional<Service> checkService = serviceService.findById(serviceId);
                if (!checkService.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay service");
                }
                service = checkService.get();
            } else if (type.equalsIgnoreCase(Constants.LOAI_YEU_CAU_CAP_QUYEN.ENDPOINT.ma())) {
                if (!body.containsKey("externalEndpointId")) {
                    return CustomResponse.Response_data(400, "Thieu thong tin");
                }
                endpointId = Integer.parseInt(body.get("externalEndpointId").toString());
                Optional<Endpoint> checkEndpoint = endpointService.findById(endpointId);
                if (!checkEndpoint.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay endpoint");
                }
                endpoint = checkEndpoint.get();
            }

            Optional<Organization> checkFrom = organizationService.findBySsId(fromId);
            Optional<Organization> checkTo = organizationService.findBySsId(toId);
            if (!checkFrom.isPresent() || !checkTo.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }

            AccessRequest accessRequest = new AccessRequest();
            accessRequest.setExternalId(externalId);
            accessRequest.setFrom(checkFrom.get());
            accessRequest.setFromName(fromName);
            accessRequest.setTo(checkTo.get());
            accessRequest.setType(type);
            accessRequest.setService(service);
            accessRequest.setEndpoint(endpoint);
            accessRequest.setExternalServiceId(null);
            accessRequest.setExternalEndpointId(null);
            accessRequest.setDescription(description);
            accessRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_CAP_QUYEN.PENDING.ma());
            accessRequest.setCreateAt(Utils.DATETIME_NOW());
            accessRequest.setUpdateAt(Utils.DATETIME_NOW());
            accessRequest.setDeclinedReason(null);
            System.out.println("================================");
            AccessRequest accessRequestRes = accessRequestService.saveAccessRequest(accessRequest);
            System.out.println("================================");

            return CustomResponse.Response_data(201, accessRequestRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{externalId}/approve")
    public ResponseEntity<Map<String, Object>> approve(@PathVariable String externalId) {
        try {
            Optional<AccessRequest> checkAccessRequest = accessRequestService.findByExternalId(externalId);
            if (!checkAccessRequest.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            AccessRequest accessRequest = checkAccessRequest.get();
            accessRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_CAP_QUYEN.APPROVED.ma());
            accessRequest.setUpdateAt(Utils.DATETIME_NOW());
            accessRequestService.saveAccessRequest(accessRequest);
            return CustomResponse.Response_no_data(204);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{externalId}/decline")
    public ResponseEntity<Map<String, Object>> add(
            @PathVariable String externalId,
            @RequestBody Map<String, Object> body) {
        try {
            String declinedReason = null;
            if (body.containsKey("declinedReason")) {
                declinedReason = (String) body.get("declinedReason");
            }
            Optional<AccessRequest> checkAccessRequest = accessRequestService.findByExternalId(externalId);
            if (!checkAccessRequest.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            AccessRequest accessRequest = checkAccessRequest.get();
            accessRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_CAP_QUYEN.DECLINED.ma());
            accessRequest.setUpdateAt(Utils.DATETIME_NOW());
            accessRequest.setDeclinedReason(declinedReason);
            accessRequestService.saveAccessRequest(accessRequest);
            return CustomResponse.Response_no_data(204);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }
}
