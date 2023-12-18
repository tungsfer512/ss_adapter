package vn.ript.ssadapter.controller;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.document.Attachment;
import vn.ript.ssadapter.model.document.DocumentCatalog;
import vn.ript.ssadapter.model.document.DocumentCatalogReport;
import vn.ript.ssadapter.model.document.DocumentCatalogRequest;
import vn.ript.ssadapter.model.document.DocumentType;
import vn.ript.ssadapter.service.DocumentCatalogReportService;
import vn.ript.ssadapter.service.DocumentCatalogRequestService;
import vn.ript.ssadapter.service.DocumentCatalogService;
import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;
import vn.ript.ssadapter.utils.minio.Minio;

@RestController
@RequestMapping("/api/v1/document-catalogs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentCatalogController {

    @Autowired
    DocumentCatalogService documentCatalogService;
    @Autowired
    DocumentCatalogReportService documentCatalogReportService;
    @Autowired
    DocumentCatalogRequestService documentCatalogRequestService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    OrganizationService organizationService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(name = "fromId", required = false) String fromId,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "promulgationPlace", required = false) String promulgationPlace,
            @RequestParam(name = "promulgationDate", required = false) String[] promulgationDate,
            @RequestParam(name = "documentTypeId", required = false) Integer documentTypeId,
            @RequestParam(name = "subject", required = false) String subject,
            @RequestParam(name = "steeringType", required = false) Integer steeringType,
            @RequestParam(name = "businessDocumentId", required = false) String businessDocumentId,
            @RequestParam(name = "enable", required = false) Boolean enable,
            @RequestParam(name = "isPublic", required = false) Boolean isPublic,
            @RequestParam(name = "allowedOrganizationId", required = false) String allowedOrganizationId,
            @RequestParam(name = "sortDate", required = false) String sortDate,
            @RequestParam(name = "sortSubject", required = false) String sortSubject) {
        try {
            String code_number = null;
            String code_notation = null;
            if (code != null && !code.equalsIgnoreCase("")) {
                String[] codes = code.split("/");
                code_number = codes[0];
                code_notation = codes[1];
            }

            List<DocumentCatalog> documentCatalogs = documentCatalogService.findWithConditions(
                    fromId,
                    code_number,
                    code_notation,
                    promulgationPlace,
                    promulgationDate,
                    documentTypeId,
                    subject,
                    steeringType,
                    businessDocumentId,
                    enable,
                    isPublic,
                    sortDate,
                    sortSubject);
            return CustomResponse.Response_data(200, documentCatalogs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> newDocument(
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @RequestPart(name = "json_data", required = true) String json_data) {
        try {
            String code_number = null;
            String code_notation = null;
            String promulgation_place = null;
            String promulgation_date = null;
            Integer document_type_id = null;
            String subject = null;
            String content = null;
            Integer other_info_page_amount = null;
            Integer steering_type = null;
            String bussiness_doc_reason = null;
            String business_document_id = null;
            Boolean is_public = true;
            Boolean enable = true;

            JSONObject jsonObject = new JSONObject(json_data);
            if (jsonObject.has("codeNumber")) {
                code_number = jsonObject.getString("codeNumber");
            }
            if (jsonObject.has("codeNotation")) {
                code_notation = jsonObject.getString("codeNotation");
            }
            if (jsonObject.has("promulgationDatePlace")) {
                promulgation_place = jsonObject.getString("promulgationDatePlace");
            }
            if (jsonObject.has("promulgationDate")) {
                promulgation_date = jsonObject.getString("promulgationDate");
            }
            if (jsonObject.has("documentTypeId")) {
                document_type_id = jsonObject.getInt("documentTypeId");
            }
            if (jsonObject.has("subject")) {
                subject = jsonObject.getString("subject");
            }
            if (jsonObject.has("content")) {
                content = jsonObject.getString("content");
            }
            if (jsonObject.has("otherInfoPageAmount")) {
                other_info_page_amount = jsonObject.getInt("otherInfoPageAmount");
            }
            if (jsonObject.has("steeringType")) {
                steering_type = jsonObject.getInt("steeringType");
            }
            if (jsonObject.has("businessBussinessDocReason")) {
                bussiness_doc_reason = jsonObject.getString("businessBussinessDocReason");
            }
            if (jsonObject.has("businessDocumentId")) {
                business_document_id = jsonObject.getString("businessDocumentId");
            }
            List<String> attachment_description_list = Utils.JSON_GET_STRING_LIST(jsonObject,
                    "attachmentDescriptionList");
            if (jsonObject.has("isPublic")) {
                is_public = jsonObject.getBoolean("isPublic");
            }
            if (jsonObject.has("enable")) {
                enable = jsonObject.getBoolean("enable");
            }
            List<Organization> allowed_organizations = null;
            if (is_public == false) {
                allowed_organizations = new ArrayList<>();
                List<String> allowed_organization_ids = Utils.JSON_GET_STRING_LIST(jsonObject,
                        "allowedOrganizationIds");
                for (String allowed_organization_id : allowed_organization_ids) {
                    Optional<Organization> checkOrganization = organizationService
                            .findByOrganId(allowed_organization_id);
                    if (checkOrganization.isPresent()) {
                        allowed_organizations.add(checkOrganization.get());
                    }
                }
            }

            List<Attachment> attachments = null;
            String senderDocId = Utils.UUID();
            if (!files.isEmpty()) {
                if (attachment_description_list != null
                        && attachment_description_list.size() == files.size()) {
                    attachments = new ArrayList<>();
                    for (int i = 0; i < files.size(); i++) {
                        File file_file = Utils.MULTIPART_FILE_TO_FILE(files.get(i),
                                Constants.LOAI_FILE.ATTACHMENT.ma());
                        Minio minio = new Minio();
                        String file_name = Utils.UUID();
                        minio.createBucketIfNotExist(Utils.SS_MINIO_BUCKET);
                        minio.uploadObject(Utils.SS_MINIO_BUCKET, file_name, file_file.getAbsolutePath().toString());
                        Attachment attachment = new Attachment();
                        attachment.setAttachmentAttachmentName(file_file.getName());
                        attachment.setAttachmentDescription(attachment_description_list.get(i));
                        attachment.setAttachmentContentId(file_name);
                        attachment.setAttachmentContentType(files.get(i).getContentType());
                        attachments.add(attachment);
                    }
                } else {
                    return CustomResponse.Response_data(400, "Thieu thong tin file dinh kem");
                }
            } else {
            }
            Optional<Organization> checkFrom = organizationService.findByOrganId(Utils.SS_ID);
            if (!checkFrom.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            Organization from = checkFrom.get();

            Optional<DocumentType> check_document_type = documentTypeService.findById(document_type_id);
            if (!check_document_type.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay loai van ban");
            }
            DocumentType document_type = check_document_type.get();
            DocumentCatalog document = new DocumentCatalog(
                    Utils.UUID(),
                    from,
                    code_number,
                    code_notation,
                    promulgation_place,
                    promulgation_date,
                    document_type,
                    subject,
                    content,
                    other_info_page_amount,
                    attachments,
                    steering_type,
                    bussiness_doc_reason,
                    business_document_id,
                    senderDocId,
                    null,
                    enable,
                    null,
                    is_public,
                    allowed_organizations);

            documentCatalogService.saveDocumentCatalog(document);

            return CustomResponse.Response_data(201, document);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDocumentById(@PathVariable String id) {
        try {
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            return CustomResponse.Response_data(200, document);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/attachments/{id}/download")
    public ResponseEntity<InputStreamResource> downloadAttachmentById(@PathVariable String id) {
        try {
            Minio minio = new Minio();
            InputStream inputStream = minio.getObject(Utils.SS_MINIO_BUCKET, id);
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return CustomResponse.Response_file(200, inputStreamResource, id);
        } catch (Exception e) {
            return CustomResponse.Response_file(500, null, null);
        }
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Map<String, Object>> enableDocument(@PathVariable String id) {
        try {
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            document.setEnable(true);
            DocumentCatalog documentRes = documentCatalogService.saveDocumentCatalog(document);
            return CustomResponse.Response_data(200, documentRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Map<String, Object>> disableDocument(@PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            String disableReason = (String) body.get("disableReason");
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            document.setEnable(false);
            document.setDisableReason(disableReason);
            DocumentCatalog documentRes = documentCatalogService.saveDocumentCatalog(document);
            return CustomResponse.Response_data(200, documentRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/{id}/access-rights")
    public ResponseEntity<Map<String, Object>> getAllAccessRightOfDocument(@PathVariable String id) {
        try {
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            return CustomResponse.Response_data(200, document.getAllowedOrganizations());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/access-rights")
    public ResponseEntity<Map<String, Object>> addAccessRightOfDocument(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            Gson gson = new Gson();
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            List<Organization> allowedOrganizations = document.getAllowedOrganizations();
            JSONArray jsonArray = new JSONArray(gson.toJson(body.get("organization_ids")));
            List<Object> objectAllowedOrganizations = jsonArray.toList();
            for (Object objectAllowedOrganization : objectAllowedOrganizations) {
                Organization organization = organizationService.findByOrganId(objectAllowedOrganization.toString())
                        .get();
                allowedOrganizations.add(organization);
            }
            Set<Organization> setOrganization = new HashSet<>(allowedOrganizations);
            document.setAllowedOrganizations(new ArrayList<Organization>(setOrganization));
            DocumentCatalog documentRes = documentCatalogService.saveDocumentCatalog(document);
            return CustomResponse.Response_data(200, documentRes.getAllowedOrganizations());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/{id}/access-rights/delete")
    public ResponseEntity<Map<String, Object>> deleteAccessRightOfDocument(
            @PathVariable String id,
            @RequestBody Map<String, Object> body) {
        try {
            Gson gson = new Gson();
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            List<Organization> allowedOrganizations = document.getAllowedOrganizations();
            JSONArray jsonArray = new JSONArray(gson.toJson(body.get("organization_ids")));
            List<Object> objectAllowedOrganizations = jsonArray.toList();
            for (Object objectAllowedOrganization : objectAllowedOrganizations) {
                Organization organization = organizationService.findByOrganId(objectAllowedOrganization.toString())
                        .get();
                if (allowedOrganizations.contains(organization)) {
                    allowedOrganizations.remove(organization);
                }
            }
            Set<Organization> setOrganization = new HashSet<>(allowedOrganizations);
            document.setAllowedOrganizations(new ArrayList<Organization>(setOrganization));
            DocumentCatalog documentRes = documentCatalogService.saveDocumentCatalog(document);
            return CustomResponse.Response_data(200, documentRes.getAllowedOrganizations());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/access-rights/requests")
    public ResponseEntity<Map<String, Object>> addRequestForAccessRightOfDocument(
            @RequestBody Map<String, Object> body) {
        try {
            String documentCatalogId = (String) body.get("documentCatalogId");
            String organizationId = (String) body.get("organizationId");
            String description = (String) body.get("description");
            String status = Constants.TRANG_THAI_YEU_CAU_THU_VIEN.PENDING.ma();
            String statusDesc = Constants.TRANG_THAI_YEU_CAU_THU_VIEN.PENDING.moTa();
            String timestamp = Utils.DATETIME_NOW();

            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(documentCatalogId);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay van ban");
            }
            DocumentCatalog document = checkDocument.get();

            Optional<Organization> checkOrganization = organizationService.findByOrganId(organizationId);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            Organization organization = checkOrganization.get();

            DocumentCatalogRequest documentCatalogRequest = new DocumentCatalogRequest();
            documentCatalogRequest.setOrganization(organization);
            documentCatalogRequest.setDocumentCatalog(document);
            documentCatalogRequest.setDescription(description);
            documentCatalogRequest.setStatus(status);
            documentCatalogRequest.setStatusDesc(statusDesc);
            documentCatalogRequest.setTimestamp(timestamp);

            DocumentCatalogRequest documentCatalogRequestRes = documentCatalogRequestService
                    .saveDocumentCatalogRequest(documentCatalogRequest);

            return CustomResponse.Response_data(200, documentCatalogRequestRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/access-rights/requests")
    public ResponseEntity<Map<String, Object>> getAllRequestForAccessRightOfDocument(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "organizationId", required = false) String organizationId,
            @RequestParam(name = "documentCatalogId", required = false) String documentCatalogId,
            @RequestParam(name = "sortTimestamp", required = false) String sortTimestamp) {
        try {
            List<DocumentCatalogRequest> documentCatalogRequests = documentCatalogRequestService
                    .findWithConditions(status, organizationId, documentCatalogId, sortTimestamp);
            return CustomResponse.Response_data(200, documentCatalogRequests);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/access-rights/requests/{id}")
    public ResponseEntity<Map<String, Object>> getRequestForAccessRightOfDocumentById(
            @PathVariable Integer id) {
        try {
            Optional<DocumentCatalogRequest> checkDocumentCatalogRequests = documentCatalogRequestService.findById(id);
            return CustomResponse.Response_data(200, checkDocumentCatalogRequests.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/access-rights/requests/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveRequestForAccessRightOfDocument(
            @PathVariable Integer id) {
        try {
            Optional<DocumentCatalogRequest> checkDocumentRequest = documentCatalogRequestService.findById(id);
            if (!checkDocumentRequest.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay yeu cau");
            }
            DocumentCatalogRequest documentRequest = checkDocumentRequest.get();
            Organization organization = documentRequest.getOrganization();
            DocumentCatalog document = documentRequest.getDocumentCatalog();
            List<Organization> allowedOrganizations = document.getAllowedOrganizations();
            allowedOrganizations.add(organization);
            Set<Organization> setOrganization = new HashSet<>(allowedOrganizations);
            document.setAllowedOrganizations(new ArrayList<Organization>(setOrganization));
            documentCatalogService.saveDocumentCatalog(document);
            documentRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_THU_VIEN.APPROVED.ma());
            documentRequest.setStatusDesc(Constants.TRANG_THAI_YEU_CAU_THU_VIEN.APPROVED.moTa());
            documentCatalogRequestService.saveDocumentCatalogRequest(documentRequest);
            return CustomResponse.Response_no_data(204);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/access-rights/requests/{id}/decline")
    public ResponseEntity<Map<String, Object>> decliineRequestForAccessRightOfDocument(
            @PathVariable Integer id) {
        try {
            Optional<DocumentCatalogRequest> checkDocumentRequest = documentCatalogRequestService.findById(id);
            if (!checkDocumentRequest.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay yeu cau");
            }
            DocumentCatalogRequest documentRequest = checkDocumentRequest.get();
            documentRequest.setStatus(Constants.TRANG_THAI_YEU_CAU_THU_VIEN.DECLINED.ma());
            documentRequest.setStatusDesc(Constants.TRANG_THAI_YEU_CAU_THU_VIEN.DECLINED.moTa());
            documentCatalogRequestService.saveDocumentCatalogRequest(documentRequest);
            return CustomResponse.Response_no_data(204);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PostMapping("/reports")
    public ResponseEntity<Map<String, Object>> addReportForAccessRightOfDocument(
            @RequestBody Map<String, Object> body) {
        try {
            String documentCatalogId = (String) body.get("documentCatalogId");
            String organizationId = (String) body.get("organizationId");
            String description = (String) body.get("description");
            String status = Constants.TRANG_THAI_BAO_CAO_THU_VIEN.PENDING.ma();
            String statusDesc = Constants.TRANG_THAI_BAO_CAO_THU_VIEN.PENDING.moTa();
            String timestamp = Utils.DATETIME_NOW();

            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(documentCatalogId);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay van ban");
            }
            DocumentCatalog document = checkDocument.get();

            Optional<Organization> checkOrganization = organizationService.findByOrganId(organizationId);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            Organization organization = checkOrganization.get();

            DocumentCatalogReport documentCatalogReport = new DocumentCatalogReport();
            documentCatalogReport.setOrganization(organization);
            documentCatalogReport.setDocumentCatalog(document);
            documentCatalogReport.setDescription(description);
            documentCatalogReport.setStatus(status);
            documentCatalogReport.setStatusDesc(statusDesc);
            documentCatalogReport.setTimestamp(timestamp);

            DocumentCatalogReport documentCatalogReportRes = documentCatalogReportService
                    .saveDocumentCatalogReport(documentCatalogReport);

            return CustomResponse.Response_data(200, documentCatalogReportRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/reports")
    public ResponseEntity<Map<String, Object>> getAllReportForAccessRightOfDocument(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "organizationId", required = false) String organizationId,
            @RequestParam(name = "documentCatalogId", required = false) String documentCatalogId,
            @RequestParam(name = "sortTimestamp", required = false) String sortTimestamp) {
        try {
            List<DocumentCatalogReport> documentCatalogReports = documentCatalogReportService
                    .findWithConditions(status, organizationId, documentCatalogId, sortTimestamp);
            return CustomResponse.Response_data(200, documentCatalogReports);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<Map<String, Object>> getReportForAccessRightOfDocumentById(
            @PathVariable Integer id) {
        try {
            Optional<DocumentCatalogReport> checkDocumentCatalogReports = documentCatalogReportService.findById(id);
            return CustomResponse.Response_data(200, checkDocumentCatalogReports.get());
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<Map<String, Object>> changeStatusReportForAccessRightOfDocument(
            @PathVariable Integer id) {
        try {
            Optional<DocumentCatalogReport> checkDocumentReport = documentCatalogReportService.findById(id);
            if (!checkDocumentReport.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay yeu cau");
            }
            DocumentCatalogReport documentReport = checkDocumentReport.get();
            Organization organization = documentReport.getOrganization();
            DocumentCatalog document = documentReport.getDocumentCatalog();
            List<Organization> allowedOrganizations = document.getAllowedOrganizations();
            allowedOrganizations.add(organization);
            Set<Organization> setOrganization = new HashSet<>(allowedOrganizations);
            document.setAllowedOrganizations(new ArrayList<Organization>(setOrganization));
            documentCatalogService.saveDocumentCatalog(document);
            documentReport.setStatus(Constants.TRANG_THAI_BAO_CAO_THU_VIEN.APPROVED.ma());
            documentReport.setStatusDesc(Constants.TRANG_THAI_BAO_CAO_THU_VIEN.APPROVED.moTa());
            documentCatalogReportService.saveDocumentCatalogReport(documentReport);
            return CustomResponse.Response_no_data(204);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
