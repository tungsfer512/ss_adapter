package vn.ript.ssadapter.controller;

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
import vn.ript.ssadapter.service.OrganizationService;
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
            List<Organization> organizations = organizationService.findAll();
            return CustomResponse.Response_data(200, organizations);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }
    @GetMapping("/{code}")
    public ResponseEntity<Map<String, Object>> getOrganizationsByCode(@PathVariable String code) {
        try {
            Optional<Organization> checkOrganization = organizationService.findByCode(code);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            return CustomResponse.Response_data(200, checkOrganization.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<Map<String, Object>> registerOrganization(
            @RequestBody Map<String, Object> entity) {
        try {
            System.out.println("entity: " + entity);
            Organization organization = new Organization();
            String UUID = Utils.UUID();
            organization.setId(UUID);
            organization.setCode((String) entity.get("code"));
            organization.setPid((String) entity.get("pid"));
            organization.setName((String) entity.get("name"));
            organization.setAddress((String) entity.get("address"));
            organization.setEmail((String) entity.get("email"));
            organization.setTelephone((String) entity.get("telephone"));
            organization.setFax((String) entity.get("fax"));
            organization.setWebsite((String) entity.get("website"));
            Organization organizationRes = organizationService.add(organization);
            return CustomResponse.Response_data(200, organizationRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    // @DeleteMapping(value = "/")
    // public ResponseEntity<Map<String, Object>> deleteOrganization(
    // @RequestHeader(name = "OrganizationCode", required = true) String
    // organizationCode) {
    // try {
    // System.out.println("OrganizationCode: " + organizationCode);
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
