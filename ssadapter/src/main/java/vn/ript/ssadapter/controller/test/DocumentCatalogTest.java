package vn.ript.ssadapter.controller.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.document.Attachment;
import vn.ript.ssadapter.model.document.DocumentCatalog;
import vn.ript.ssadapter.model.document.DocumentType;
import vn.ript.ssadapter.service.DocumentCatalogService;
import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("api/v1/test/tvdt")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentCatalogTest {

    @Autowired
    DocumentCatalogService documentCatalogService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    OrganizationService organizationService;

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
                        Attachment attachment = new Attachment();
                        attachment.setAttachmentAttachmentName(file_file.getName());
                        attachment.setAttachmentDescription(attachment_description_list.get(i));
                        attachment.setAttachmentContentId(file_file.getAbsolutePath());
                        attachments.add(attachment);
                    }
                } else {
                    return CustomResponse.Response_data(400, "Thieu thong tin file dinh kem");
                }
            } else {
                System.out.println("Khong co file dinh kem");
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
    public ResponseEntity<Map<String, Object>> disableDocument(@PathVariable String id, @RequestBody String disableReason) {
        try {

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

}
