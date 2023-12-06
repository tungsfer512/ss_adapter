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
            List<Organization> organizations = organizationService.findAllExcept(Utils.SS_ID);
            return CustomResponse.Response_data(200, organizations);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }
    @GetMapping("/{organId}")
    public ResponseEntity<Map<String, Object>> getOrganizationsByOrganId(@PathVariable String organId) {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(organId);
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
            @RequestBody Map<String, Object> entity) {
        try {
            Organization organization = new Organization();
            String UUID = Utils.UUID();
            organization.setId(UUID);
            organization.setOrganId(entity.get("organId").toString());
            organization.setOrganizationInCharge(entity.get("organizationInCharge").toString());
            organization.setOrganName(entity.get("organName").toString());
            organization.setOrganAdd(entity.get("organAdd").toString());
            organization.setEmail(entity.get("email").toString());
            organization.setTelephone(entity.get("telephone").toString());
            organization.setFax(entity.get("fax").toString());
            organization.setWebsite(entity.get("website").toString());
            Organization organizationRes = organizationService.save(organization);
            return CustomResponse.Response_data(200, organizationRes);
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
