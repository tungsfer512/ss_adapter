package vn.ript.base.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vnpt.xml.base.Content;

import vn.ript.base.model.Organization;
import vn.ript.base.model.document.Document;
import vn.ript.base.service.DocumentService;
import vn.ript.base.service.OrganizationService;
import vn.ript.base.utils.Constants;
import vn.ript.base.utils.CustomHttpRequest;
import vn.ript.base.utils.CustomResponse;
import vn.ript.base.utils.EdXMLBuild;
import vn.ript.base.utils.Utils;

@RestController
@RequestMapping("/api/v1/document/status")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentStatusController {

    @Autowired
    DocumentService documentService;
    @Autowired
    OrganizationService organizationService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> sendStatusEdoc(
            @RequestHeader(name = "docId", required = true) String docId,
            @RequestBody(required = true) Map<String, String> status_info) {
        try {

            String status_staff_info_department = null;
            String status_staff_info_staff = null;
            String status_staff_info_mobile = null;
            String status_staff_info_email = null;
            String status_status_code = "";
            String status_description = null;
            if (status_info.containsKey("statusStaffInfoDepartment")) {
                status_staff_info_department = status_info.get("statusStaffInfoDepartment");
            }
            if (status_info.containsKey("statusStaffInfoStaff")) {
                status_staff_info_staff = status_info.get("statusStaffInfoStaff");
            }
            if (status_info.containsKey("statusStaffInfoMobile")) {
                status_staff_info_mobile = status_info.get("statusStaffInfoMobile");
            }
            if (status_info.containsKey("status_StaffInfoEmail")) {
                status_staff_info_email = status_info.get("statusStaffInfoEmail");
            }
            if (status_info.containsKey("statusStatusCode")) {
                status_status_code = status_info.get("statusStatusCode");
            }
            if (status_info.containsKey("statusDescription")) {
                status_description = status_info.get("statusDescription");
            }

            Optional<Document> check_document = documentService.findByDocumentId(docId);
            if (!check_document.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay van ban can phan hoi");
            }
            Document document = check_document.get();

            String tmp_to_id = document.getFrom().getOrganId();
            String tmp_from_id = document.getTo().getOrganId();
            Optional<Organization> checkTo = organizationService.findByOrganId(tmp_to_id);
            Optional<Organization> checkFrom = organizationService.findByOrganId(tmp_from_id);
            if (!checkFrom.isPresent() || !checkTo.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }

            Content content = EdXMLBuild.create_status(
                    checkFrom.get(),
                    document,
                    status_staff_info_department,
                    status_staff_info_staff,
                    status_staff_info_mobile,
                    status_staff_info_email,
                    status_status_code,
                    status_description);
            String document_64 = Utils.ENCODE_TO_BASE64(content.getContent());

            String subsystem_code = checkTo.get().getOrganId().replace(':', '/');
            String xRoadClient = tmp_from_id.replace(':', '/');
            String url = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                        "/" + Utils.SS_QLVB_SERVICE_CODE + "/document/status/new";
            Map<String, String> headers = new HashMap<>();
            headers.put("docId", docId);
            headers.put("X-Road-Client", xRoadClient);
            CustomHttpRequest customHttpRequest = new CustomHttpRequest("POST", url, headers);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody("file", content.getContent());
            HttpEntity multipartHttpEntity = multipartEntityBuilder.build();

            HttpResponse httpResponse = customHttpRequest.request(multipartHttpEntity);

            String status = Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.maTrangThai();
            String statusDesc = Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.moTaTrangThai();
            if (httpResponse.getStatusLine().getStatusCode() != 201) {
                status = Constants.TRANG_THAI_LIEN_THONG.THAT_BAI.maTrangThai();
                statusDesc = Constants.TRANG_THAI_LIEN_THONG.THAT_BAI.moTaTrangThai();
            } else {
                document.setSendStatus(Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).maTrangThai());
                document.setReceiveStatus(
                        Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).maTrangThai());
                document.setSendStatusDesc(
                        Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).moTaTrangThaiGui()
                                + "\n (" + status_description + ")");
                document.setReceiveStatusDesc(
                        Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).moTaTrangThaiNhan()
                                + "\n (" + status_description + ")");
                documentService.updateDocument(document);
            }

            String status_tmp = Constants.TRANG_THAI_GOI_TIN.DA_GUI_KHONG_PHAN_HOI.maTrangThai();
            if (status_status_code.equalsIgnoreCase(Constants.TRANG_THAI_VAN_BAN.DA_YEU_CAU_CAP_NHAT.maTrangThai())
                    || status_status_code
                            .equalsIgnoreCase(Constants.TRANG_THAI_VAN_BAN.DA_YEU_CAU_CAP_NHAT.maTrangThai())) {
                status_tmp = Constants.TRANG_THAI_GOI_TIN.CHO_PHAN_HOI.maTrangThai();
            }

            Document documentStatus = new Document(
                    Utils.UUID(),
                    checkFrom.get(),
                    checkTo.get(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    document.getFrom().getOrganId(),
                    document.getCodeCodeNumber() + "/" + document.getCodeCodeNotation(),
                    document.getPromulgationInfoPromulgationDate(),
                    document.getDocumentId(),
                    null,
                    null,
                    status_status_code,
                    status_description,
                    Utils.DATETIME_NOW(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "eDoc",
                    "status",
                    null,
                    null,
                    status,
                    statusDesc,
                    Constants.TRANG_THAI_GOI_TIN.getByMaTrangThai(status_tmp).maTrangThai(),
                    Constants.TRANG_THAI_GOI_TIN.getByMaTrangThai(status_tmp).maTrangThai(),
                    Constants.TRANG_THAI_GOI_TIN.getByMaTrangThai(status_tmp).moTaTrangThaiGui(),
                    Constants.TRANG_THAI_GOI_TIN.getByMaTrangThai(status_tmp).moTaTrangThaiNhan(),
                    document_64);
            documentService.saveDocument(documentStatus);
            return CustomResponse.Response_no_data(201);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/getReceivedStatusEdocList")
    public ResponseEntity<Map<String, Object>> getReceivedStatusEdocList(
            @RequestHeader(name = "organizationId", required = true) String organization_id) {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(organization_id);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<Document> edocs = documentService.getReceivedStatusEdocList(organization_id);
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/getSentStatusEdocList")
    public ResponseEntity<Map<String, Object>> getSentStatusEdocList(
            @RequestHeader(name = "organizationId", required = true) String organization_id) {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(organization_id);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<Document> edocs = documentService.getSentStatusEdocList(organization_id);
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
