package vn.ript.base.controller.apilienthong;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import vn.ript.base.model.Organization;
import vn.ript.base.model.document.Attachment;
import vn.ript.base.model.document.DocumentCatalog;
import vn.ript.base.model.document.DocumentCatalogReport;
import vn.ript.base.model.document.DocumentCatalogRequest;
import vn.ript.base.model.document.DocumentType;
import vn.ript.base.service.DocumentCatalogReportService;
import vn.ript.base.service.DocumentCatalogRequestService;
import vn.ript.base.service.DocumentCatalogService;
import vn.ript.base.service.DocumentTypeService;
import vn.ript.base.service.OrganizationService;
import vn.ript.base.utils.Constants;
import vn.ript.base.utils.CustomResponse;
import vn.ript.base.utils.Utils;
import vn.ript.base.utils.minio.Minio;

@RestController
@RequestMapping("/api/lienthong/v1/document-catalogs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentCatalogLienThongController {

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

    Boolean isDocumentCatalogOwner(String organizationId, DocumentCatalog documentCatalog) {
        String ownerId = documentCatalog.getFrom().getOrganId();
        if (ownerId.equalsIgnoreCase(organizationId)) {
            return true;
        } else {
            return false;
        }
    }

    Boolean isDocumentCatalogRequestOwner(String organizationId, DocumentCatalogRequest documentCatalogRequest) {
        String ownerId = documentCatalogRequest.getOrganization().getOrganId();
        if (ownerId.equalsIgnoreCase(organizationId)) {
            return true;
        } else {
            return false;
        }
    }

    Boolean isDocumentCatalogRequestOwnerOrDocumentCatalogOwner(String organizationId,
            DocumentCatalogRequest documentCatalogRequest) {
        String ownerId = documentCatalogRequest.getOrganization().getOrganId();
        String documentCatalogOwnerId = documentCatalogRequest.getDocumentCatalog().getFrom().getOrganId();
        if (ownerId.equalsIgnoreCase(organizationId) || documentCatalogOwnerId.equalsIgnoreCase(organizationId)) {
            return true;
        } else {
            return false;
        }
    }

    Boolean isDocumentCatalogReportOwner(String organizationId, DocumentCatalogReport documentCatalogReport) {
        String ownerId = documentCatalogReport.getOrganization().getOrganId();
        if (ownerId.equalsIgnoreCase(organizationId)) {
            return true;
        } else {
            return false;
        }
    }

    Boolean isDocumentCatalogReportOwnerOrDocumentCatalogOwner(String organizationId,
            DocumentCatalogReport documentCatalogReport) {
        String ownerId = documentCatalogReport.getOrganization().getOrganId();
        String documentCatalogOwnerId = documentCatalogReport.getDocumentCatalog().getFrom().getOrganId();
        if (ownerId.equalsIgnoreCase(organizationId) || documentCatalogOwnerId.equalsIgnoreCase(organizationId)) {
            return true;
        } else {
            return false;
        }
    }

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
            String from_id = null;
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

            JSONObject jsonObject = new JSONObject((json_data));
            System.out.println(json_data);
            System.out.println(jsonObject);
            System.out.println(jsonObject.toString());
            if (jsonObject.has("fromId")) {
                from_id = jsonObject.getString("fromId");
                System.out.println(from_id);
            }
            if (jsonObject.has("codeNumber")) {
                code_number = jsonObject.getString("codeNumber");
                System.out.println(code_number);
            }
            if (jsonObject.has("codeNotation")) {
                code_notation = jsonObject.getString("codeNotation");
                System.out.println(code_notation);
            }
            if (jsonObject.has("promulgationDatePlace")) {
                promulgation_place = jsonObject.getString("promulgationDatePlace");
                System.out.println(promulgation_place);
            }
            if (jsonObject.has("promulgationDate")) {
                promulgation_date = jsonObject.getString("promulgationDate");
                System.out.println(promulgation_date);
            }
            if (jsonObject.has("documentTypeId")) {
                System.out.println(document_type_id);
                document_type_id = jsonObject.getInt("documentTypeId");
            }
            if (jsonObject.has("subject")) {
                subject = jsonObject.getString("subject");
                System.out.println(subject);
            }
            if (jsonObject.has("content")) {
                content = jsonObject.getString("content");
                System.out.println(content);
            }
            if (jsonObject.has("otherInfoPageAmount")) {
                System.out.println(other_info_page_amount);
                other_info_page_amount = jsonObject.getInt("otherInfoPageAmount");
            }
            if (jsonObject.has("steeringType")) {
                System.out.println(steering_type);
                steering_type = jsonObject.getInt("steeringType");
            }
            if (jsonObject.has("businessBussinessDocReason")) {
                bussiness_doc_reason = jsonObject.getString("businessBussinessDocReason");
                System.out.println(bussiness_doc_reason);
            }
            if (jsonObject.has("businessDocumentId")) {
                business_document_id = jsonObject.getString("businessDocumentId");
                System.out.println(business_document_id);
            }
            List<String> attachment_description_list = Utils.JSON_GET_STRING_LIST(jsonObject,
                    "attachmentDescriptionList");
            if (jsonObject.has("isPublic")) {
                is_public = jsonObject.getBoolean("isPublic");
                System.out.println(is_public);
            }
            if (jsonObject.has("enable")) {
                enable = jsonObject.getBoolean("enable");
                System.out.println(enable);
            }
            System.out.println("+++++++++++++++++++++++++1");
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
            System.out.println("+++++++++++++++++++++++++2");
            List<Attachment> attachments = null;
            String senderDocId = Utils.UUID();
            System.out.println(files);
            System.out.println(files.getClass().toString());
            if (!files.isEmpty()) {
                System.out.println("+++++++++++++++++++++++++2-x");
                if (attachment_description_list != null
                        && attachment_description_list.size() >= files.size()) {
                    System.out.println("+++++++++++++++++++++++++2-x");
                    attachments = new ArrayList<>();
                    for (int i = 0; i < files.size(); i++) {
                        System.out.println("+++++++++++++++++++++++++2-xx1");
                        File file_file = Utils.MULTIPART_FILE_TO_FILE(files.get(i),
                                Constants.LOAI_FILE.ATTACHMENT.ma());
                        System.out.println("+++++++++++++++++++++++++2-xx2");
                        Minio minio = new Minio();
                        System.out.println("+++++++++++++++++++++++++2-xx3");
                        String file_name = Utils.UUID();
                        System.out.println("+++++++++++++++++++++++++2-xx4");
                        minio.createBucketIfNotExist(Utils.SS_MINIO_BUCKET);
                        System.out.println("+++++++++++++++++++++++++2-xx5");
                        minio.uploadObject(Utils.SS_MINIO_BUCKET, file_name, file_file.getAbsolutePath().toString());
                        System.out.println("+++++++++++++++++++++++++2-xx6");
                        Attachment attachment = new Attachment();
                        System.out.println("+++++++++++++++++++++++++2-x7");
                        attachment.setAttachmentAttachmentName(file_file.getName());
                        System.out.println("+++++++++++++++++++++++++2-x8");
                        attachment.setAttachmentDescription(attachment_description_list.get(i));
                        System.out.println("+++++++++++++++++++++++++2-xx9");
                        attachment.setAttachmentContentId(file_name);
                        System.out.println("+++++++++++++++++++++++++2-xx10");
                        attachment.setAttachmentContentType(files.get(i).getContentType());
                        System.out.println("+++++++++++++++++++++++++2-xx11");
                        attachments.add(attachment);
                        System.out.println("+++++++++++++++++++++++++2-xx12");
                    }
                } else {
                    return CustomResponse.Response_data(400, "Thieu thong tin file dinh kem");
                }
            }
            System.out.println("+++++++++++++++++++++++++3");
            Optional<Organization> checkFrom = organizationService.findByOrganId(from_id);
            if (!checkFrom.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            System.out.println("+++++++++++++++++++++++++4");
            Organization from = checkFrom.get();
            System.out.println("+++++++++++++++++++++++++5");

            Optional<DocumentType> check_document_type = documentTypeService.findById(document_type_id);
            if (!check_document_type.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay loai van ban");
            }
            System.out.println("+++++++++++++++++++++++++6");
            DocumentType document_type = check_document_type.get();
            System.out.println("+++++++++++++++++++++++++7");
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

            System.out.println("+++++++++++++++++++++++++8");
            documentCatalogService.saveDocumentCatalog(document);
            System.out.println("+++++++++++++++++++++++++9");

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
    public ResponseEntity<Map<String, Object>> enableDocument(
            @PathVariable String id,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient) {
        try {
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogOwner(interactorId, document);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai chu so huu tai lieu !!!");
            }
            document.setEnable(true);
            DocumentCatalog documentRes = documentCatalogService.saveDocumentCatalog(document);
            return CustomResponse.Response_data(200, documentRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Map<String, Object>> disableDocument(
            @PathVariable String id,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("disableReason")) {
                return CustomResponse.Response_data(400, "Thieu thong tin !!!");
            }
            String disableReason = (String) body.get("disableReason");
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogOwner(interactorId, document);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai chu so huu tai lieu !!!");
            }
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
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("organization_ids")) {
                return CustomResponse.Response_data(400, "Thieu thong tin !!!");
            }
            Gson gson = new Gson();
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogOwner(interactorId, document);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai chu so huu tai lieu !!!");
            }
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
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient,
            @RequestBody Map<String, Object> body) {
        try {
            if (!body.containsKey("organization_ids")) {
                return CustomResponse.Response_data(400, "Thieu thong tin !!!");
            }
            Gson gson = new Gson();
            Optional<DocumentCatalog> checkDocument = documentCatalogService.findById(id);
            if (!checkDocument.isPresent()) {
                return CustomResponse.Response_no_data(404);
            }
            DocumentCatalog document = checkDocument.get();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogOwner(interactorId, document);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai chu so huu tai lieu !!!");
            }
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
            if (!body.containsKey("documentCatalogId") || !body.containsKey("organizationId")
                    || !body.containsKey("description")) {
                return CustomResponse.Response_data(400, "Thieu thong tin !!!");
            }
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
            if (!checkOrganization.isPresent()) {
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
            @RequestParam(name = "type", required = true) String type,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "documentCatalogId", required = false) String documentCatalogId,
            @RequestParam(name = "sortTimestamp", required = false) String sortTimestamp,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient) {
        try {
            String interactorId = xRoadClient.replace('/', ':');
            if (type.equalsIgnoreCase("sent")) {
                List<DocumentCatalogRequest> documentCatalogRequests = documentCatalogRequestService
                        .findSentWithConditions(status, interactorId, documentCatalogId, sortTimestamp);
                return CustomResponse.Response_data(200, documentCatalogRequests);
            } else if (type.equalsIgnoreCase("received")) {
                List<DocumentCatalogRequest> documentCatalogRequests = documentCatalogRequestService
                        .findReceivedWithConditions(status, interactorId, documentCatalogId, sortTimestamp);
                return CustomResponse.Response_data(200, documentCatalogRequests);
            } else {
                return CustomResponse.Response_data(400, "Co loi xay ra");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/access-rights/requests/{id}")
    public ResponseEntity<Map<String, Object>> getRequestForAccessRightOfDocumentById(
            @PathVariable Integer id,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient) {
        try {
            Optional<DocumentCatalogRequest> checkDocumentCatalogRequests = documentCatalogRequestService.findById(id);
            DocumentCatalogRequest documentCatalogRequest = checkDocumentCatalogRequests.get();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogRequestOwnerOrDocumentCatalogOwner(interactorId,
                    documentCatalogRequest);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai la nguoi gui yeu cau hoac chu so huu tai lieu !!!");
            }
            return CustomResponse.Response_data(200, documentCatalogRequest);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/access-rights/requests/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveRequestForAccessRightOfDocument(
            @PathVariable Integer id,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient) {
        try {
            Optional<DocumentCatalogRequest> checkDocumentRequest = documentCatalogRequestService.findById(id);
            if (!checkDocumentRequest.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay yeu cau");
            }
            DocumentCatalogRequest documentRequest = checkDocumentRequest.get();
            Organization organization = documentRequest.getOrganization();
            DocumentCatalog document = documentRequest.getDocumentCatalog();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogOwner(interactorId, document);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai chu so huu tai lieu !!!");
            }
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
            @PathVariable Integer id,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient) {
        try {
            Optional<DocumentCatalogRequest> checkDocumentRequest = documentCatalogRequestService.findById(id);
            if (!checkDocumentRequest.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay yeu cau");
            }
            DocumentCatalogRequest documentRequest = checkDocumentRequest.get();
            DocumentCatalog document = documentRequest.getDocumentCatalog();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogOwner(interactorId, document);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai chu so huu tai lieu !!!");
            }
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
            if (!body.containsKey("documentCatalogId") || !body.containsKey("organizationId")
                    || !body.containsKey("description")) {
                return CustomResponse.Response_data(400, "Thieu thong tin !!!");
            }
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
            if (!checkOrganization.isPresent()) {
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
            @RequestParam(name = "type", required = true) String type,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "documentCatalogId", required = false) String documentCatalogId,
            @RequestParam(name = "sortTimestamp", required = false) String sortTimestamp,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient) {
        try {
            String interactorId = xRoadClient.replace('/', ':');
            if (type.equalsIgnoreCase("sent")) {
                List<DocumentCatalogReport> documentCatalogReports = documentCatalogReportService
                        .findSentWithConditions(status, interactorId, documentCatalogId, sortTimestamp);
                return CustomResponse.Response_data(200, documentCatalogReports);
            } else if (type.equalsIgnoreCase("received")) {
                List<DocumentCatalogReport> documentCatalogReports = documentCatalogReportService
                        .findReceivedWithConditions(status, interactorId, documentCatalogId, sortTimestamp);
                return CustomResponse.Response_data(200, documentCatalogReports);
            } else {
                return CustomResponse.Response_data(400, "Co loi xay ra");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<Map<String, Object>> getReportForAccessRightOfDocumentById(
            @PathVariable Integer id,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient) {
        try {
            Optional<DocumentCatalogReport> checkDocumentCatalogReports = documentCatalogReportService.findById(id);
            DocumentCatalogReport documentCatalogReport = checkDocumentCatalogReports.get();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogReportOwnerOrDocumentCatalogOwner(interactorId,
                    documentCatalogReport);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai la nguoi gui bao cao hoac chu so huu tai lieu !!!");
            }
            return CustomResponse.Response_data(200, documentCatalogReport);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<Map<String, Object>> changeStatusReportForAccessRightOfDocument(
            @PathVariable Integer id,
            @RequestHeader(name = "X-Road-Client", required = true) String xRoadClient) {
        try {
            Optional<DocumentCatalogReport> checkDocumentReport = documentCatalogReportService.findById(id);
            if (!checkDocumentReport.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay yeu cau");
            }
            DocumentCatalogReport documentReport = checkDocumentReport.get();
            Organization organization = documentReport.getOrganization();
            DocumentCatalog document = documentReport.getDocumentCatalog();
            String interactorId = xRoadClient.replace('/', ':');
            Boolean isOwnerInteract = isDocumentCatalogOwner(interactorId, document);
            if (!isOwnerInteract) {
                return CustomResponse.Response_data(403, "Khong co quyen do ban khong phai chu so huu tai lieu !!!");
            }
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
