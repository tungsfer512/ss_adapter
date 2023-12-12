package vn.ript.ssadapter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.model.document.DocumentCatalog;
import vn.ript.ssadapter.service.DocumentCatalogService;
import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.CustomResponse;

@RestController
@RequestMapping("/api/v1/document-catalogs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentCatalogController {

    @Autowired
    DocumentCatalogService documentCatalogService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    OrganizationService organizationService;

    // @PostMapping("/new")
    // public ResponseEntity<Map<String, Object>> sendDocument(
    // @RequestPart(name = "files", required = false) List<MultipartFile> files,
    // @RequestPart(name = "json_data", required = true) String json_data) {
    // try {
    // JSONObject jsonObject = new JSONObject(json_data);
    // String code_number = jsonObject.getString("codeNumber");
    // String code_notation = jsonObject.getString("codeNotation");
    // String promulgation_place = jsonObject.getString("promulgationDatePlace");
    // String promulgation_date = jsonObject.getString("promulgationDate");
    // Integer document_type_id = jsonObject.getInt("documentTypeId");
    // String subject = jsonObject.getString("subject");
    // String content = jsonObject.getString("content");
    // Integer page_amount = jsonObject.getInt("otherInfoPageAmount");
    // List<String> appendixes = Utils.JSON_GET_STRING_LIST(jsonObject,
    // "appendixes");
    // Integer steering_type = jsonObject.getInt("steeringType");
    // Integer business_bussiness_doc_type =
    // jsonObject.getInt("businessBussinessDocType");
    // String bussiness_doc_reason =
    // jsonObject.getString("businessBussinessDocReason");
    // String business_document_id = jsonObject.getString("businessDocumentId");
    // List<String> attachment_description_list =
    // Utils.JSON_GET_STRING_LIST(jsonObject,
    // "attachmentDescriptionList");
    // Boolean is_public = Boolean.valueOf(jsonObject.getString("isPublic"));
    // Boolean enable = Boolean.valueOf(jsonObject.getString("enable"));
    // List<Organization> allowed_organizations = null;
    // if (is_public == false) {
    // allowed_organizations = new ArrayList<>();
    // List<String> allowed_organization_ids =
    // Utils.JSON_GET_STRING_LIST(jsonObject,
    // "allowedOrganizationIds");
    // for (String allowed_organization_id : allowed_organization_ids) {
    // Optional<Organization> checkOrganization = organizationService
    // .findByOrganId(allowed_organization_id);
    // if (checkOrganization.isPresent()) {
    // allowed_organizations.add(checkOrganization.get());
    // }
    // }
    // }

    // String senderDocId = Utils.UUID();
    // List<File> attachments = null;
    // if (!files.isEmpty()) {
    // attachments = new ArrayList<>();
    // for (MultipartFile file : files) {
    // attachments.add(Utils.MULTIPART_FILE_TO_FILE(file,
    // Constants.LOAI_FILE.ATTACHMENT.ma()));
    // }
    // }
    // Optional<Organization> checkFrom =
    // organizationService.findByOrganId(Utils.SS_ID);
    // if (!checkFrom.isPresent()) {
    // return CustomResponse.Response_data(404, "Khong tim thay don vi");
    // }
    // Organization from = checkFrom.get();

    // Optional<DocumentType> check_document_type =
    // documentTypeService.findById(document_type_id);
    // if (!check_document_type.isPresent()) {
    // return CustomResponse.Response_data(404, "Khong tim thay loai van ban");
    // }
    // DocumentType document_type = check_document_type.get();

    // // for (Organization to : to_list) {
    // // String subsystem_code = to.getOrganId().replace(':', '/');
    // // String xRoadClient = Utils.SS_ID.replace(':', '/');
    // // String url = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
    // // "/thuviendientu/documents/new";
    // // Map<String, String> headers = new HashMap<>();
    // // headers.put("from", Utils.SS_ID);
    // // headers.put("X-Road-Client", xRoadClient);

    // // CustomHttpRequest customHttpRequest = new CustomHttpRequest("POST", url,
    // // headers);

    // // MultipartEntityBuilder multipartEntityBuilder =
    // MultipartEntityBuilder.create();
    // // multipartEntityBuilder.addBinaryBody("file", edXmlContent.getContent());
    // // HttpEntity multipartHttpEntity = multipartEntityBuilder.build();

    // // HttpResponse httpResponse =
    // customHttpRequest.request(multipartHttpEntity);
    // // }

    // DocumentCatalog document = new DocumentCatalog(
    // Utils.UUID(),
    // from,
    // code_number,
    // code_notation,
    // promulgation_place,
    // promulgation_date,
    // document_type,
    // subject,
    // content,
    // page_amount,
    // null,
    // steering_type,
    // bussiness_doc_reason,
    // business_document_id,
    // // null,
    // senderDocId,
    // null,
    // enable,
    // is_public,
    // allowed_organizations);
    // documentCatalogService.saveDocumentCatalog(document);
    // return CustomResponse.Response_data(201, document);
    // } catch (Exception e) {
    // return CustomResponse.Response_data(500, e.toString());
    // }
    // }

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

}
