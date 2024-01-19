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

import vn.ript.ssmanageadapter.model.initialize.Endpoint;
import vn.ript.ssmanageadapter.service.initialize.EndpointService;
import vn.ript.ssmanageadapter.utils.CustomResponse;

@RestController
@RequestMapping("/api/lienthong/v1/endpoints")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EndpointLienThongController {
    @Autowired
    EndpointService endpointService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getEndpointsList() {
        try {
            List<Endpoint> endpoints = endpointService.findAll();
            return CustomResponse.Response_data(200, endpoints);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/{ssId}")
    public ResponseEntity<Map<String, Object>> getEndpointsBySsId(@PathVariable String ssId) {
        try {
            Optional<Endpoint> checkEndpoint = endpointService.findBySsId(ssId);
            if (!checkEndpoint.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay endpoint");
            }
            return CustomResponse.Response_data(200, checkEndpoint.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addEndpoint(
            @RequestBody Map<String, Object> body) {
        try {
            Endpoint endpoint = new Endpoint();
            endpoint.setSsId(body.get("ssId").toString());
            endpoint.setName(body.get("name").toString());
            endpoint.setMethod(body.get("method").toString());
            endpoint.setPath(body.get("path").toString());
            endpoint.setDescription(body.get("description").toString());
            endpoint.setInputDescription(body.get("inputDescription").toString());
            endpoint.setOutputDescription(body.get("outputDescription").toString());
            endpoint.setIsPublic(Boolean.valueOf(body.get("isPublic").toString()));
            endpoint.setIsForCitizen(Boolean.valueOf(body.get("isForCitizen").toString()));
            endpoint.setType(body.get("type").toString());
            Endpoint endpointRes = endpointService.save(endpoint);
            return CustomResponse.Response_data(201, endpointRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }
}
