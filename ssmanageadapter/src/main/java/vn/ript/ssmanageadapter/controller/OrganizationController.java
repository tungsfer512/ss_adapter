package vn.ript.ssmanageadapter.controller;

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

import vn.ript.ssmanageadapter.model.Organization;
import vn.ript.ssmanageadapter.service.OrganizationService;
import vn.ript.ssmanageadapter.utils.CustomResponse;
import vn.ript.ssmanageadapter.utils.Utils;

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

    @GetMapping("/{ssId}")
    public ResponseEntity<Map<String, Object>> getOrganizationsBySsId(@PathVariable String ssId) {
        try {
            Optional<Organization> checkOrganization = organizationService.findBySsId(ssId);
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
            @RequestBody Map<String, Object> body) {
        try {
            Organization organization = new Organization();
            String UUID = Utils.UUID();
            organization.setId(UUID);
            organization.setOrganId(body.get("organId").toString());
            organization.setSsId(body.get("ssId").toString());
            organization.setOrganizationInCharge(body.get("organizationInCharge").toString());
            organization.setOrganName(body.get("organName").toString());
            organization.setOrganAdd(body.get("organAdd").toString());
            organization.setEmail(body.get("email").toString());
            organization.setTelephone(body.get("telephone").toString());
            organization.setFax(body.get("fax").toString());
            organization.setWebsite(body.get("website").toString());
            Organization organizationRes = organizationService.save(organization);
            return CustomResponse.Response_data(201, organizationRes);
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
