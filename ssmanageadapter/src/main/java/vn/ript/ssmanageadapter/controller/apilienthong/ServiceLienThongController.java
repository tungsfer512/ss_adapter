package vn.ript.ssmanageadapter.controller.apilienthong;

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

import vn.ript.ssmanageadapter.model.initialize.Service;
import vn.ript.ssmanageadapter.service.initialize.ServiceService;
import vn.ript.ssmanageadapter.utils.CustomResponse;

@RestController
@RequestMapping("/api/lienthong/v1/services")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ServiceLienThongController {
    @Autowired
    ServiceService serviceService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getServicesList() {
        try {
            List<Service> services = serviceService.findAll();
            return CustomResponse.Response_data(200, services);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/{ssId}")
    public ResponseEntity<Map<String, Object>> getServicesBySsId(@PathVariable String ssId) {
        try {
            Optional<Service> checkService = serviceService.findBySsId(ssId);
            if (!checkService.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay service");
            }
            return CustomResponse.Response_data(200, checkService.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addService(
            @RequestBody Map<String, Object> body) {
        try {
            Service service = new Service();
            service.setSsId(body.get("ssId").toString());
            service.setDescription(body.get("description").toString());
            service.setIsPublic(Boolean.valueOf(body.get("isPublic").toString()));
            service.setIsForCitizen(Boolean.valueOf(body.get("isForCitizen").toString()));
            service.setType(body.get("type").toString());
            Service serviceRes = serviceService.save(service);
            return CustomResponse.Response_data(201, serviceRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }
}
