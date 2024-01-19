package vn.ript.ssmanageadapter.controller.apilienthong;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssmanageadapter.model.Organization;
import vn.ript.ssmanageadapter.model.initialize.ServiceDescription;
import vn.ript.ssmanageadapter.service.OrganizationService;
import vn.ript.ssmanageadapter.service.initialize.ServiceDescriptionService;
import vn.ript.ssmanageadapter.utils.CustomResponse;

@RestController
@RequestMapping("/api/lienthong/v1/service-descriptions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServiceDescriptionLienThongController {
    @Autowired
    ServiceDescriptionService serviceDescriptionService;
    @Autowired
    OrganizationService organizationService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getServiceDescriptionsList() {
        try {
            List<ServiceDescription> serviceDescriptions = serviceDescriptionService.findAll();
            return CustomResponse.Response_data(200, serviceDescriptions);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/{ssId}")
    public ResponseEntity<Map<String, Object>> getServiceDescriptionsBySsId(@PathVariable String ssId) {
        try {
            Optional<ServiceDescription> checkServiceDescription = serviceDescriptionService.findBySsId(ssId);
            if (!checkServiceDescription.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay service description");
            }
            return CustomResponse.Response_data(200, checkServiceDescription.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addServiceDescription(
            @RequestBody Map<String, Object> body) {
        try {
            ServiceDescription service = new ServiceDescription();
            service.setSsId(body.get("ssId").toString());
            service.setDescription(body.get("description").toString());
            Optional<Organization> checkOrganization = organizationService.findBySsId(body.get("organSsId").toString());
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            service.setOrganization(checkOrganization.get());
            ServiceDescription serviceRes = serviceDescriptionService.save(service);
            return CustomResponse.Response_data(201, serviceRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PatchMapping("")
    public ResponseEntity<Map<String, Object>> editServiceDescription(
            @RequestBody Map<String, Object> body) {
        try {

            Optional<ServiceDescription> checkServiceDescription = serviceDescriptionService
                    .findBySsId(body.get("ssId").toString());
            ServiceDescription serviceDescription = checkServiceDescription.get();
            serviceDescription.setSsId(body.get("ssId").toString());
            serviceDescription.setDescription(body.get("description").toString());
            Optional<Organization> checkOrganization = organizationService.findBySsId(body.get("organSsId").toString());
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            serviceDescription.setOrganization(checkOrganization.get());
            ServiceDescription serviceRes = serviceDescriptionService.save(serviceDescription);
            return CustomResponse.Response_data(200, serviceRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }
}
