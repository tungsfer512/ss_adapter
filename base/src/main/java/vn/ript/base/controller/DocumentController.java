package vn.ript.base.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vnpt.xml.base.Content;
import com.vnpt.xml.base.header.Header;
import com.vnpt.xml.ed.Ed;
import com.vnpt.xml.ed.header.MessageHeader;

import vn.ript.base.model.Organization;
import vn.ript.base.model.document.Attachment;
import vn.ript.base.model.document.Document;
import vn.ript.base.model.document.DocumentType;
import vn.ript.base.model.document.ReplacementInfo;
import vn.ript.base.model.document.TraceHeader;
import vn.ript.base.model.document.UpdateReceiver;
import vn.ript.base.service.DocumentService;
import vn.ript.base.service.DocumentTypeService;
import vn.ript.base.service.OrganizationService;
import vn.ript.base.utils.Constants;
import vn.ript.base.utils.CustomHttpRequest;
import vn.ript.base.utils.CustomResponse;
import vn.ript.base.utils.EdXML;
import vn.ript.base.utils.EdXMLBuild;
import vn.ript.base.utils.Utils;

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
            @RequestPart(name = "json_data", required = true) String json_data) {
        try {
            String from_id = null;
            String code_number = null;
            String code_notation = null;
            String promulgation_place = null;
            Integer document_type_id = null;
            String subject = null;
            String content = null;
            String signer_info_competence = null;
            String signer_info_position = null;
            String signer_info_fullname = null;
            String due_date = null;
            Integer other_info_priority = null;
            String other_info_sphere_of_promulgation = null;
            String other_info_typer_notation = null;
            Integer other_info_promulgation_amount = null;
            Integer other_info_page_amount = null;
            String response_for_document_id = null;
            Integer steering_type = null;
            Integer business_bussiness_doc_type = null;
            String business_bussiness_doc_reason = null;
            Integer business_bussiness_document_info_document_info = null;
            Integer business_bussiness_document_info_document_receiver = null;
            String business_document_id = null;
            String business_staff_info_department = null;
            String business_staff_info_staff = null;
            String business_staff_info_mobile = null;
            String business_staff_info_email = null;
            Integer business_paper = null;

            JSONObject jsonObject = new JSONObject(json_data);
            System.out.println("============================1");
            List<String> to_ids = Utils.JSON_GET_STRING_LIST(jsonObject, "toIds");
            if (jsonObject.has("fromId")) {
                from_id = jsonObject.getString("fromId");
            } else {
                return CustomResponse.Response_data(400, "Thieu thong tin !!!"); 
            }
            System.out.println("============================2");
            if (jsonObject.has("codeNumber")) {
                code_number = jsonObject.getString("codeNumber");
            }
            if (jsonObject.has("codeNotation")) {
                code_notation = jsonObject.getString("codeNotation");
            }
            if (jsonObject.has("promulgationPlace")) {
                promulgation_place = jsonObject.getString("promulgationPlace");
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
            if (jsonObject.has("signerInfoCompetence")) {
                signer_info_competence = jsonObject.getString("signerInfoCompetence");
            }
            if (jsonObject.has("signerInfoPosition")) {
                signer_info_position = jsonObject.getString("signerInfoPosition");
            }
            if (jsonObject.has("signerInfoFullname")) {
                signer_info_fullname = jsonObject.getString("signerInfoFullname");
            }
            if (jsonObject.has("dueDate")) {
                due_date = jsonObject.getString("dueDate");
            }
            List<String> to_places = Utils.JSON_GET_STRING_LIST(jsonObject, "toPlaces");
            if (jsonObject.has("otherInfoPriority")) {
                other_info_priority = jsonObject.getInt("otherInfoPriority");
            }
            if (jsonObject.has("otherInfoSphereOfPromulgation")) {
                other_info_sphere_of_promulgation = jsonObject.getString("otherInfoSphereOfPromulgation");
            }
            if (jsonObject.has("otherInfoTyperNotation")) {
                other_info_typer_notation = jsonObject.getString("otherInfoTyperNotation");
            }
            if (jsonObject.has("otherInfoPromulgationAmount")) {
                other_info_promulgation_amount = jsonObject.getInt("otherInfoPromulgationAmount");
            }
            if (jsonObject.has("otherInfoPageAmount")) {
                other_info_page_amount = jsonObject.getInt("otherInfoPageAmount");
            }
            List<String> appendixes = Utils.JSON_GET_STRING_LIST(jsonObject, "appendixes");
            if (jsonObject.has("responseForDocumentId")) {
                response_for_document_id = jsonObject.getString("responseForDocumentId");
            }
            if (jsonObject.has("steeringType")) {
                steering_type = jsonObject.getInt("steeringType");
            }
            if (jsonObject.has("businessBussinessDocType")) {
                business_bussiness_doc_type = jsonObject.getInt("businessBussinessDocType");
            }
            if (jsonObject.has("businessBussinessDocReason")) {
                business_bussiness_doc_reason = jsonObject.getString("businessBussinessDocReason");
            }
            if (jsonObject.has("businessBussinessDocumentInfoDocumentInfo")) {
                business_bussiness_document_info_document_info = jsonObject
                .getInt("businessBussinessDocumentInfoDocumentInfo");
            }
            if (jsonObject.has("businessBussinessDocumentInfoDocumentReceiver")) {
                business_bussiness_document_info_document_receiver = jsonObject
                .getInt("businessBussinessDocumentInfoDocumentReceiver");
            }
            List<String> business_bussiness_document_info_receiver_json_str_list = Utils.JSON_GET_STRING_LIST(
                jsonObject,
                "businessBussinessDocumentInfoReceiverJsonStrList");
                if (jsonObject.has("businessDocumentId")) {
                    business_document_id = jsonObject.getString("businessDocumentId");
            }
            if (jsonObject.has("businessStaffInfoDepartment")) {
                business_staff_info_department = jsonObject.getString("businessStaffInfoDepartment");
            }
            if (jsonObject.has("businessStaffInfoStaff")) {
                business_staff_info_staff = jsonObject.getString("businessStaffInfoStaff");
            }
            if (jsonObject.has("businessStaffInfoMobile")) {
                business_staff_info_mobile = jsonObject.getString("businessStaffInfoMobile");
            }
            if (jsonObject.has("businessStaffInfoEmail")) {
                business_staff_info_email = jsonObject.getString("businessStaffInfoEmail");
            }
            if (jsonObject.has("businessPaper")) {
                business_paper = jsonObject.getInt("businessPaper");
            }
            System.out.println("============================3");
            List<String> business_replacement_info_json_str_list = Utils.JSON_GET_STRING_LIST(jsonObject,
            "businessReplacementInfoJsonStrList");
            List<String> attachment_description_list = Utils.JSON_GET_STRING_LIST(jsonObject,
            "attachmentDescriptionList");
            
            System.out.println("============================4");
            Content edXmlContent;
            String senderDocId = Utils.UUID();
            List<File> attachments = new ArrayList<>();
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    attachments.add(Utils.MULTIPART_FILE_TO_FILE(file, Constants.LOAI_FILE.ATTACHMENT.ma()));
                }
            }
            System.out.println("============================5");
            Optional<Organization> checkFrom = organizationService.findByOrganId(from_id);
            if (!checkFrom.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi gui");
            }
            Organization from = checkFrom.get();
            System.out.println("============================6");
            List<Organization> to_list = new ArrayList<>();
            for (String to_id : to_ids) {
                Optional<Organization> checkTo = organizationService.findByOrganId(to_id);
                if (!checkTo.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi nhan");
                }
                to_list.add(checkTo.get());
            }
            DocumentType document_type = null;
            if (document_type_id != null) {
                Optional<DocumentType> check_document_type = documentTypeService.findById(document_type_id);
                if (!check_document_type.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay loai van ban");
                }
                document_type = check_document_type.get();
            }
            
            System.out.println("============================7");
            List<UpdateReceiver> business_bussiness_document_info_receiver_list = new ArrayList<>();
            for (String business_bussiness_document_info_receiver_json_str : business_bussiness_document_info_receiver_json_str_list) {
                JSONObject jsonReceiverObject = new JSONObject(
                        business_bussiness_document_info_receiver_json_str);
                Integer updateReceiverReceiverType = null;
                String updateReceiverOrganId = null;
                if (jsonReceiverObject.has("updateReceiverReceiverType")) {
                    updateReceiverReceiverType = jsonReceiverObject.getInt("updateReceiverReceiverType");
                }
                if (jsonReceiverObject.has("updateReceiverOrganId")) {
                    updateReceiverOrganId = jsonReceiverObject.getString("updateReceiverOrganId");
                }
                UpdateReceiver updateReceiver = new UpdateReceiver(
                        updateReceiverReceiverType,
                        updateReceiverOrganId);
                business_bussiness_document_info_receiver_list.add(updateReceiver);
            }

            List<ReplacementInfo> business_replacement_info_list = new ArrayList<>();
            for (String business_replacement_info_json_str : business_replacement_info_json_str_list) {
                JSONObject jsonReplacementObject = new JSONObject(business_replacement_info_json_str);
                ReplacementInfo replacementInfo = new ReplacementInfo();
                String replacementInfoDocumentId = null;
                if (jsonReplacementObject.has("replacementInfoDocumentId")) {
                    replacementInfoDocumentId = jsonReplacementObject.getString("replacementInfoDocumentId");
                }
                replacementInfo.setReplacementInfoDocumentId(replacementInfoDocumentId);
                List<String> replacement_info_organ_id_list = new ArrayList<>();
                List<Object> tmp = new ArrayList<>();
                if (jsonReplacementObject.has("replacementInfoOrganIdList")) {
                    tmp = jsonReplacementObject.getJSONArray("replacementInfoOrganIdList").toList();
                }
                for (Object t : tmp) {
                    replacement_info_organ_id_list.add(t.toString());
                }
                replacementInfo.setReplacementInfoOrganIdList(replacement_info_organ_id_list);
                business_replacement_info_list.add(replacementInfo);
            }

            Document response_for_document = null;
            String response_for_code = null;
            String response_for_promulgation_date = null;
            if (response_for_document_id != null) {
                Optional<Document> check_response_for_document = documentService
                        .findByDocumentId(response_for_document_id);
                if (check_response_for_document.isPresent()) {
                    response_for_document = check_response_for_document.get();
                    response_for_code = response_for_document.getCodeCodeNumber() + "/"
                            + response_for_document.getCodeCodeNotation();
                    response_for_promulgation_date = response_for_document.getPromulgationInfoPromulgationDate();
                }
            }

            edXmlContent = EdXMLBuild.createEdoc_new(
                    from,
                    to_list,
                    code_number,
                    code_notation,
                    promulgation_place,
                    null,
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
                    response_for_document,
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
            List<TraceHeader> trace_headers = new ArrayList<>();

            com.vnpt.xml.base.header.TraceHeaderList traceHeaderList = header.getTraceHeaderList();
            if (traceHeaderList != null) {
                if (traceHeaderList.getTraceHeaders() != null) {
                    com.vnpt.xml.base.header.TraceHeader xml_trace_header = traceHeaderList.getTraceHeaders()
                            .get(0);
                    if (xml_trace_header != null) {
                        TraceHeader traceHeader = new TraceHeader();
                        traceHeader.setTraceHeaderOrganId(xml_trace_header.getOrganId());
                        traceHeader.setTraceHeaderTimestamp(
                                Utils.DATE_TO_YYYY_MM_DD_HH_MM_SS(xml_trace_header.getTimestamp()));
                        trace_headers.add(traceHeader);
                    }
                }
            }

            List<Attachment> adapter_attachments = new ArrayList<>();
            List<com.vnpt.xml.base.attachment.Attachment> edoc_attachments = ed.getAttachments();
            for (com.vnpt.xml.base.attachment.Attachment edoc_attachment : edoc_attachments) {
                Attachment adapter_attachment = new Attachment(
                        edoc_attachment.getContentType(),
                        edoc_attachment.getContentId(),
                        edoc_attachment.getDescription(),
                        edoc_attachment.getContentTransferEncoded(),
                        edoc_attachment.getName());
                adapter_attachments.add(adapter_attachment);
            }

            for (String to_id : to_ids) {
                Optional<Organization> checkTo = organizationService.findByOrganId(to_id);
                if (!checkTo.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                }
                String subsystem_code = to_id.replace(':', '/');
                String xRoadClient = from_id.replace(':', '/');
                String url = Utils.SS_BASE_URL + "/r1/" + subsystem_code +
                        "/" + Utils.SS_QLVB_SERVICE_CODE + "/document/edocs/new";
                Map<String, String> headers = new HashMap<>();
                headers.put("from", from_id);
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
                        to_id,
                        response_for_code,
                        response_for_promulgation_date,
                        response_for_document_id,
                        steering_type,
                        docIdEdxml,
                        null,
                        null,
                        null,
                        trace_headers,
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
                        adapter_attachments,
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

    @GetMapping("/getReceivedEdocList")
    public ResponseEntity<Map<String, Object>> getReceivedEdocList(
            @RequestHeader(name = "organizationId", required = true) String organization_id) {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(organization_id);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<Document> edocs = documentService.getReceivedEdocList(checkOrganization.get().getId());
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e.toString());
        }
    }

    @GetMapping("/getSentEdocList")
    public ResponseEntity<Map<String, Object>> getSentEdocList(
            @RequestHeader(name = "organizationId", required = true) String organization_id) {
        try {
            Optional<Organization> checkOrganization = organizationService.findByOrganId(organization_id);
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
