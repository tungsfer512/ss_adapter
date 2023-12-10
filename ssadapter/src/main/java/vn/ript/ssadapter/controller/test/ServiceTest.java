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

import vn.ript.ssadapter.model.initialize.Service;
import vn.ript.ssadapter.model.initialize.ServiceDescription;
import vn.ript.ssadapter.service.initialize.ServiceDescriptionService;
import vn.ript.ssadapter.service.initialize.ServiceService;
import vn.ript.ssadapter.utils.CustomResponse;

@RestController
@RequestMapping("api/v1/test/services")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServiceTest {

    @Autowired
    ServiceService serviceService;

    @Autowired
    ServiceDescriptionService serviceDescriptionService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getServicesList() {
        try {
            List<Service> services = serviceService.findAll();
            return CustomResponse.Response_data(200, services);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getServicesById(@PathVariable Integer id) {
        try {
            Optional<Service> checkService = serviceService.findById(id);
            if (!checkService.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            return CustomResponse.Response_data(200, checkService.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> registerService(
            @RequestBody Map<String, Object> body) {
        try {
            Service service = new Service();
            Integer serviceDescriptionId = (Integer) body.get("serviceDescriptionId");
            service.setSsId((String) body.get("ssId"));
            service.setDescription((String) body.get("description"));
            Optional<ServiceDescription> checkServiceDescription = serviceDescriptionService.findById(serviceDescriptionId);
            if (checkServiceDescription.isPresent()) {
                Service serviceRes = serviceService.save(service);
                ServiceDescription serviceDescription = checkServiceDescription.get();
                List<Service> services = serviceDescription.getServices();
                services.add(serviceRes);
                serviceDescriptionService.save(serviceDescription);
                return CustomResponse.Response_data(200, serviceRes);
            } else {
                return CustomResponse.Response_data(404, "Khong tim thay");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
