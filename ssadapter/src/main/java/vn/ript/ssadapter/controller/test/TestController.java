package vn.ript.ssadapter.controller.test;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.CustomResponse;

@RestController
@RequestMapping("/api/lienthong/v1/test/document/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestController {

    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    OrganizationService organizationService;

    // Gui file edxml giua cac SS, extract file, cac thong tin va luu vao DB cua ben
    // nhan
    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> sendDocument(
            @RequestPart(name = "file", required = true) MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                return CustomResponse.Response_no_data(201);
            } else {
                return CustomResponse.Response_data(400, "Thieu file edxml");

            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
