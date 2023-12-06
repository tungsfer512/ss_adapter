package vn.ript.ssadapter.controller.test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import vn.ript.ssadapter.model.initialize.ServiceDescription;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.service.initiallize.ServiceDescriptionService;
import vn.ript.ssadapter.utils.CustomResponse;

@RestController
@RequestMapping("api/v1/test/service-descriptions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServiceDescriptionController {

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

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getServiceDescriptionsById(@PathVariable Integer id) {
        try {
            Optional<ServiceDescription> checkServiceDescription = serviceDescriptionService.findById(id);
            if (!checkServiceDescription.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            return CustomResponse.Response_data(200, checkServiceDescription.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> registerServiceDescription(
            @RequestBody Map<String, Object> body) {
        try {
            ServiceDescription serviceDescription = new ServiceDescription();
            String organizationId = (String) body.get("organization_id");
            serviceDescription.setSsId((String) body.get("ss_id"));
            serviceDescription.setDescription((String) body.get("description"));
            Optional<Organization> checkOrganization = organizationService.findByOrganId(organizationId);
            if (checkOrganization.isPresent()) {
                serviceDescription.setOrganization(checkOrganization.get());
                ServiceDescription serviceDescriptionRes = serviceDescriptionService.save(serviceDescription);
                return CustomResponse.Response_data(200, serviceDescriptionRes);
            } else {
                return CustomResponse.Response_data(404, "Khong tim thay");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
