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

import vn.ript.ssadapter.model.initialize.Endpoint;
import vn.ript.ssadapter.model.initialize.Service;
import vn.ript.ssadapter.service.initialize.EndpointService;
import vn.ript.ssadapter.service.initialize.ServiceService;
import vn.ript.ssadapter.utils.CustomResponse;

@RestController
@RequestMapping("api/v1/test/endpoints")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EndpointTest {

    @Autowired
    EndpointService endpointService;

    @Autowired
    ServiceService serviceService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getEndpointsList() {
        try {
            List<Endpoint> endpoints = endpointService.findAll();
            return CustomResponse.Response_data(200, endpoints);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEndpointsById(@PathVariable Integer id) {
        try {
            Optional<Endpoint> checkEndpoint = endpointService.findById(id);
            if (!checkEndpoint.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            return CustomResponse.Response_data(200, checkEndpoint.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> registerEndpoint(
            @RequestBody Map<String, Object> body) {
        try {
            Endpoint endpoint = new Endpoint();
            Integer serviceId = (Integer) body.get("serviceId");
            endpoint.setSsId((String) body.get("ssId"));
            endpoint.setName((String) body.get("name"));
            endpoint.setPath((String) body.get("path"));
            endpoint.setMethod((String) body.get("method"));
            endpoint.setDescription((String) body.get("description"));
            endpoint.setInputDescription((String) body.get("inputDescription"));
            endpoint.setOutputDescription((String) body.get("outputDescription"));
            Optional<Service> checkService = serviceService.findById(serviceId);
            if (checkService.isPresent()) {
                Service service = checkService.get();
                Endpoint endpointRes = endpointService.save(endpoint);
                List<Endpoint> endpoints = service.getEndpoints();
                endpoints.add(endpointRes);
                endpointService.save(endpointRes);
                return CustomResponse.Response_data(200, endpointRes);
            } else {
                return CustomResponse.Response_data(404, "Khong tim thay");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
