package vn.ript.ssadapter.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vnpt.xml.base.Content;
import com.vnpt.xml.base.header.Header;
import com.vnpt.xml.ed.Ed;
import com.vnpt.xml.ed.header.MessageHeader;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.document.Document;
import vn.ript.ssadapter.model.document.DocumentType;
import vn.ript.ssadapter.model.document.ReplacementInfo;
import vn.ript.ssadapter.model.document.UpdateReceiver;
import vn.ript.ssadapter.service.DocumentService;
import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXML;
import vn.ript.ssadapter.utils.EdXMLBuild;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/v1/document/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentController {

    @Autowired
    DocumentService documentService;
    @Autowired
    DocumentTypeService documentTypeService;
    @Autowired
    OrganizationService organizationService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> sendDocument(
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @RequestPart(name = "json_data", required = false) String json_data
    // @RequestPart(name = "to_ids", required = true) List<String> to_ids,
    // @RequestPart(name = "code_number", required = true) String code_number,
    // @RequestPart(name = "code_notation", required = true) String code_notation,
    // @RequestPart(name = "promulgation_place", required = true) String
    // promulgation_place,
    // @RequestPart(name = "document_type_id", required = true) Integer
    // document_type_id,
    // @RequestPart(name = "subject", required = true) String subject,
    // @RequestPart(name = "content", required = true) String content,
    // @RequestPart(name = "signer_info_competence", required = true) String
    // signer_info_competence,
    // @RequestPart(name = "signer_info_position", required = true) String
    // signer_info_position,
    // @RequestPart(name = "signer_info_fullname", required = true) String
    // signer_info_fullname,
    // @RequestPart(name = "due_date", required = true) String due_date,
    // @RequestPart(name = "to_places", required = true) List<String> to_places,
    // @RequestPart(name = "other_info_priority", required = true) Integer
    // other_info_priority,
    // @RequestPart(name = "other_info_sphere_of_promulgation", required = true)
    // String other_info_sphere_of_promulgation,
    // @RequestPart(name = "other_info_typer_notation", required = true) String
    // other_info_typer_notation,
    // @RequestPart(name = "other_info_promulgation_amount", required = true)
    // Integer other_info_promulgation_amount,
    // @RequestPart(name = "other_info_page_amount", required = true) Integer
    // other_info_page_amount,
    // @RequestPart(name = "appendixes", required = true) List<String> appendixes,
    // @RequestPart(name = "response_for", required = true) Boolean response_for,
    // @RequestPart(name = "steering_type", required = true) Integer steering_type,
    // @RequestPart(name = "status_status_code", required = true) String
    // status_status_code,
    // @RequestPart(name = "status_description", required = true) String
    // status_description,
    // @RequestPart(name = "status_timestamp", required = true) String
    // status_timestamp,
    // @RequestPart(name = "business_bussiness_doc_type", required = true) Integer
    // business_bussiness_doc_type,
    // @RequestPart(name = "business_bussiness_doc_reason", required = true) String
    // business_bussiness_doc_reason,
    // @RequestPart(name = "business_bussiness_document_info_document_info",
    // required = true) Integer business_bussiness_document_info_document_info,
    // @RequestPart(name = "business_bussiness_document_info_document_receiver",
    // required = true) Integer business_bussiness_document_info_document_receiver,
    // @RequestPart(name =
    // "business_bussiness_document_info_receiver_json_str_list", required = true)
    // List<String> business_bussiness_document_info_receiver_json_str_list,
    // @RequestPart(name = "business_document_id", required = true) String
    // business_document_id,
    // @RequestPart(name = "business_staff_info_department", required = true) String
    // business_staff_info_department,
    // @RequestPart(name = "business_staff_info_staff", required = true) String
    // business_staff_info_staff,
    // @RequestPart(name = "business_staff_info_mobile", required = true) String
    // business_staff_info_mobile,
    // @RequestPart(name = "business_staff_info_email", required = true) String
    // business_staff_info_email,
    // @RequestPart(name = "business_paper", required = true) Integer
    // business_paper,
    // @RequestPart(name = "business_replacement_info_json_str_list", required =
    // true) List<String> business_replacement_info_json_str_list,
    // @RequestPart(name = "attachment_description_list", required = true)
    // List<String> attachment_description_list
    ) {
        try {
            JSONObject jsonObject = new JSONObject(json_data);
            List<String> to_ids = Utils.JSON_GET_STRING_LIST(jsonObject, "to_ids");
            String code_number = jsonObject.getString("code_number");
            String code_notation = jsonObject.getString("code_notation");
            String promulgation_place = jsonObject.getString("promulgation_place");
            Integer document_type_id = jsonObject.getInt("document_type_id");
            String subject = jsonObject.getString("subject");
            String content = jsonObject.getString("content");
            String signer_info_competence = jsonObject.getString("signer_info_competence");
            String signer_info_position = jsonObject.getString("signer_info_position");
            String signer_info_fullname = jsonObject.getString("signer_info_fullname");
            String due_date = jsonObject.getString("due_date");
            List<String> to_places = Utils.JSON_GET_STRING_LIST(jsonObject, "to_places");
            Integer other_info_priority = jsonObject.getInt("other_info_priority");
            String other_info_sphere_of_promulgation = jsonObject
                    .getString("other_info_sphere_of_promulgation");
            String other_info_typer_notation = jsonObject.getString("other_info_typer_notation");
            Integer other_info_promulgation_amount = jsonObject.getInt("other_info_promulgation_amount");
            Integer other_info_page_amount = jsonObject.getInt("other_info_page_amount");
            List<String> appendixes = Utils.JSON_GET_STRING_LIST(jsonObject, "appendixes");
            Boolean response_for = jsonObject.getBoolean("response_for");
            Integer steering_type = jsonObject.getInt("steering_type");
            // String status_status_code = jsonObject.getString("status_status_code");
            // String status_description = jsonObject.getString("status_description");
            // String status_timestamp = jsonObject.getString("status_timestamp");
            Integer business_bussiness_doc_type = jsonObject.getInt("business_bussiness_doc_type");
            String business_bussiness_doc_reason = jsonObject.getString("business_bussiness_doc_reason");
            Integer business_bussiness_document_info_document_info = jsonObject
                    .getInt("business_bussiness_document_info_document_info");
            Integer business_bussiness_document_info_document_receiver = jsonObject
                    .getInt("business_bussiness_document_info_document_receiver");
            List<String> business_bussiness_document_info_receiver_json_str_list = Utils.JSON_GET_STRING_LIST(
                    jsonObject,
                    "business_bussiness_document_info_receiver_json_str_list");
            String business_document_id = jsonObject.getString("business_document_id");
            String business_staff_info_department = jsonObject.getString("business_staff_info_department");
            String business_staff_info_staff = jsonObject.getString("business_staff_info_staff");
            String business_staff_info_mobile = jsonObject.getString("business_staff_info_mobile");
            String business_staff_info_email = jsonObject.getString("business_staff_info_email");
            Integer business_paper = jsonObject.getInt("business_paper");
            List<String> business_replacement_info_json_str_list = Utils.JSON_GET_STRING_LIST(jsonObject,
                    "business_replacement_info_json_str_list");
            List<String> attachment_description_list = Utils.JSON_GET_STRING_LIST(jsonObject,
                    "attachment_description_list");

            Content edXmlContent;
            String senderDocId = Utils.UUID();
            List<File> attachments = null;
            if (!files.isEmpty()) {
                attachments = new ArrayList<>();
                for (MultipartFile file : files) {
                    attachments.add(Utils.MULTIPART_FILE_TO_FILE(file, Constants.LOAI_FILE.ATTACHMENT.ma()));
                }
            }
            Optional<Organization> checkFrom = organizationService.findByOrganId(Utils.SS_ID);
            if (!checkFrom.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            Organization from = checkFrom.get();
            List<Organization> to_list = new ArrayList<>();
            for (String to_id : to_ids) {
                Optional<Organization> checkTo = organizationService.findByOrganId(to_id);
                if (!checkTo.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                }
                to_list.add(checkTo.get());
            }
            TimeUnit.SECONDS.sleep(3);

            Optional<DocumentType> check_document_type = documentTypeService.findById(document_type_id);
            if (!check_document_type.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay loai van ban");
            }

            List<UpdateReceiver> business_bussiness_document_info_receiver_list = new ArrayList<>();
            for (String business_bussiness_document_info_receiver_json_str : business_bussiness_document_info_receiver_json_str_list) {
                JSONObject jsonReceiverObject = new JSONObject(
                        business_bussiness_document_info_receiver_json_str);
                UpdateReceiver updateReceiver = new UpdateReceiver(
                        jsonReceiverObject.getInt("update_receiver_receiver_type"),
                        jsonReceiverObject.getString("update_receiver_organ_id"));
                business_bussiness_document_info_receiver_list.add(updateReceiver);
            }

            List<ReplacementInfo> business_replacement_info_list = new ArrayList<>();
            for (String business_replacement_info_json_str : business_replacement_info_json_str_list) {
                JSONObject jsonReplacementObject = new JSONObject(business_replacement_info_json_str);
                ReplacementInfo replacementInfo = new ReplacementInfo();
                replacementInfo
                        .setReplacementInfo_DocumentId(jsonReplacementObject
                                .getString("replacement_info_document_id"));
                List<String> replacement_info_organ_id_list = new ArrayList<>();
                List<Object> tmp = jsonReplacementObject.getJSONArray("replacement_info_organ_id_list")
                        .toList();
                for (Object t : tmp) {
                    replacement_info_organ_id_list.add(t.toString());
                }
                replacementInfo.setReplacementInfo_OrganIdList(replacement_info_organ_id_list);
                business_replacement_info_list.add(replacementInfo);
            }

            DocumentType document_type = check_document_type.get();

            edXmlContent = EdXMLBuild.createEdoc_new(
                    from,
                    to_list,
                    code_number,
                    code_notation,
                    promulgation_place,
                    document_type,
                    subject,
                    content,
                    signer_info_competence,
                    signer_info_position,
                    signer_info_fullname,
                    due_date,
                    to_places,
                    other_info_priority,
                    other_info_sphere_of_promulgation,
                    other_info_typer_notation,
                    other_info_promulgation_amount,
                    other_info_page_amount,
                    appendixes,
                    response_for,
                    steering_type,
                    business_bussiness_doc_type,
                    business_bussiness_doc_reason,
                    business_bussiness_document_info_document_info,
                    business_bussiness_document_info_document_receiver,
                    business_bussiness_document_info_receiver_list,
                    business_document_id,
                    business_staff_info_department,
                    business_staff_info_staff,
                    business_staff_info_mobile,
                    business_staff_info_email,
                    business_paper,
                    business_replacement_info_list,
                    attachments,
                    attachment_description_list);

            String document_64 = Utils.ENCODE_TO_BASE64(edXmlContent.getContent());

            Ed ed = EdXML.readDocument(edXmlContent.getContent());
            Header header = ed.getHeader();
            MessageHeader messageHeader = (MessageHeader) header.getMessageHeader();
            String docIdEdxml = messageHeader.getDocumentId();

            for (String to_id : to_ids) {
                Optional<Organization> checkTo = organizationService.findByOrganId(to_id);
                if (!checkTo.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                }
                String subsystem_code = to_id.replace(':', '/');
                String xRoadClient = Utils.SS_ID.replace(':', '/');
                String url = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                        "/lienthongvanban/document/edocs/new";
                Map<String, String> headers = new HashMap<>();
                headers.put("from", Utils.SS_ID);
                headers.put("X-Road-Client", xRoadClient);

                CustomHttpRequest customHttpRequest = new CustomHttpRequest("POST", url,
                        headers);

                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
                multipartEntityBuilder.addBinaryBody("file", edXmlContent.getContent());
                HttpEntity multipartHttpEntity = multipartEntityBuilder.build();

                HttpResponse httpResponse = customHttpRequest.request(multipartHttpEntity);
                String status = Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.maTrangThai();
                String statusDesc = Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.moTaTrangThai();
                if (httpResponse.getStatusLine().getStatusCode() != 201) {
                    status = Constants.TRANG_THAI_LIEN_THONG.THAT_BAI.maTrangThai();
                    statusDesc = Constants.TRANG_THAI_LIEN_THONG.THAT_BAI.moTaTrangThai();
                }

                Document document = new Document(
                        Utils.UUID(),
                        from,
                        checkTo.get(),
                        code_number,
                        code_notation,
                        promulgation_place,
                        Utils.DATE_NOW(),
                        document_type,
                        subject,
                        content,
                        signer_info_competence,
                        signer_info_position,
                        signer_info_fullname,
                        due_date,
                        to_places,
                        other_info_priority,
                        other_info_sphere_of_promulgation,
                        other_info_typer_notation,
                        other_info_promulgation_amount,
                        other_info_page_amount,
                        appendixes,
                        null,
                        null,
                        null,
                        null,
                        steering_type,
                        docIdEdxml,
                        null,
                        null,
                        null,
                        null,
                        business_bussiness_doc_type,
                        business_bussiness_doc_reason,
                        business_bussiness_document_info_document_info,
                        business_bussiness_document_info_document_receiver,
                        business_bussiness_document_info_receiver_list,
                        business_document_id,
                        business_staff_info_department,
                        business_staff_info_staff,
                        business_staff_info_mobile,
                        business_staff_info_email,
                        business_paper,
                        business_replacement_info_list,
                        null,
                        "eDoc",
                        "edoc",
                        senderDocId,
                        null,
                        status,
                        statusDesc,
                        Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.maTrangThai(),
                        Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.maTrangThai(),
                        Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.moTaTrangThaiGui(),
                        Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.moTaTrangThaiNhan(),
                        document_64);

                documentService.saveDocument(document);
            }
            return CustomResponse.Response_no_data(201);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    // Get danh sach VBDT, goi tin trang thai da nhan
    @GetMapping("/getReceivedEdocList")
    public ResponseEntity<Map<String, Object>> getReceivedEdocList() {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(Utils.SS_ID);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<Document> edocs = documentService.getReceivedEdocList(checkOrganization.get().getId());
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    // Get danh sach VBDT, goi tin trang thai da gui
    @GetMapping("/getSentEdocList")
    public ResponseEntity<Map<String, Object>> getSentEdocList() {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(Utils.SS_ID);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<Document> edocs = documentService.getSentEdocList(checkOrganization.get().getId());
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

}
