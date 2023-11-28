package vn.ript.ssadapter.controller;

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

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.document.Document;
import vn.ript.ssadapter.service.DocumentService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXMLBuild;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/v1/document/status")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentStatusController {

    @Autowired
    DocumentService documentService;
    @Autowired
    OrganizationService organizationService;

    @PostMapping(value = "/update")
    public ResponseEntity<Map<String, Object>> sendStatusEdoc(
            @RequestHeader(name = "docId", required = true) String docId,
            @RequestBody(required = true) Map<String, String> status_info) {
        try {

            String status_staff_info_department = status_info.get("status_staff_info_department");
            String status_staff_info_staff = status_info.get("status_staff_info_staff");
            String status_staff_info_mobile = status_info.get("status_staff_info_mobile");
            String status_staff_info_email = status_info.get("status_staff_info_email");
            String status_status_code = status_info.get("status_status_code");
            String status_description = status_info.get("status_description");

            Optional<Document> check_document = documentService.findByDocumentId(docId);
            if (!check_document.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay van ban can phan hoi");
            }
            Document document = check_document.get();

            Optional<Organization> checkFrom = organizationService.findByOrganId(Utils.SS_ID);
            String tmp_to_id = document.getFrom().getOrganId();
            if (tmp_to_id.equalsIgnoreCase(Utils.SS_ID)) {
                tmp_to_id = document.getTo().getOrganId();
            }
            Optional<Organization> checkTo = organizationService.findByOrganId(tmp_to_id);
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
            String document_64 = Utils.encodeToBase64(content.getContent());

            String subsystem_code = checkTo.get().getOrganId().replace(':', '/');
            System.out.println(subsystem_code);
            String xRoadClient = Utils.SS_ID.replace(':', '/');
            System.out.println(xRoadClient);
            String url = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/lienthongvanban/document/status/update";
            System.out.println(url);
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
                System.out.println(httpResponse);
            } else {
                document.setSendStatus(Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).maTrangThai());
                document.setReceiveStatus(
                        Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).maTrangThai());
                document.setSendStatusDesc(
                        Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).moTaTrangThaiGui());
                document.setReceiveStatusDesc(
                        Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).moTaTrangThaiNhan());
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
                    document.getCode_CodeNumber() + "/" + document.getCode_CodeNotation(),
                    document.getPromulgationInfo_PromulgationDate(),
                    document.getDocumentId(),
                    null,
                    null,
                    status_status_code,
                    status_description,
                    Utils.datetime_now(),
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

    // Get danh sach VBDT, goi tin trang thai da nhan
    @GetMapping("/getReceivedStatusEdocList")
    public ResponseEntity<Map<String, Object>> getReceivedStatusEdocList() {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(Utils.SS_ID);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<Document> edocs = documentService.getReceivedStatusEdocList(Utils.SS_ID);
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    // Get danh sach VBDT, goi tin trang thai da gui
    @GetMapping("/getSentStatusEdocList")
    public ResponseEntity<Map<String, Object>> getSentStatusEdocList() {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(Utils.SS_ID);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<Document> edocs = documentService.getSentStatusEdocList(Utils.SS_ID);
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
