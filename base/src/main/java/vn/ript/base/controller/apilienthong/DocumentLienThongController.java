package vn.ript.base.controller.apilienthong;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vnpt.xml.base.header.ResponseFor;
import com.vnpt.xml.ed.header.PromulgationInfo;

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
import vn.ript.base.utils.CustomResponse;
import vn.ript.base.utils.EdXML;
import vn.ript.base.utils.Utils;

@RestController
@RequestMapping("/api/lienthong/v1/document/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentLienThongController {

    @Autowired
    DocumentService documentService;
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
            String receiverDocId = Utils.UUID();
            if (!file.isEmpty()) {

                Organization from = null;
                Organization to = null;
                String code_number = null;
                String code_notation = null;
                String promulgation_place = null;
                String promulgation_date = null;
                String subject = null;
                String content = null;
                String due_date = null;
                Integer document_type_type = null;
                String document_type_name = null;
                Integer other_info_priority = null;
                String other_info_sphere_of_promulgation = null;
                String other_info_typer_notation = null;
                Integer other_info_promulgation_amount = null;
                Integer other_info_page_amount = null;
                String signer_info_competence = null;
                String signer_info_position = null;
                String signer_info_fullname = null;
                Integer steering_type = null;
                String docIdEdxml = null;
                Integer business_bussiness_document_info_document_info = null;
                Integer business_bussiness_document_info_document_receiver = null;
                String business_staff_info_department = null;
                String business_staff_info_staff = null;
                String business_staff_info_mobile = null;
                String business_staff_info_email = null;
                Integer business_bussiness_doc_type = null;
                String business_bussiness_doc_reason = null;
                String business_document_id = null;
                Integer business_paper = null;
                DocumentType document_type = null;
                String response_for_organ_id = null;
                String response_for_code = null;
                String response_for_promulgation_date = null;
                String response_for_document_id = null;
                List<String> appendixes = new ArrayList<>();
                List<String> to_places = new ArrayList<>();
                List<UpdateReceiver> business_bussiness_document_info_receiver_list = new ArrayList<>();
                List<ReplacementInfo> business_replacement_info_list = new ArrayList<>();
                List<TraceHeader> trace_headers = new ArrayList<>();
                List<Attachment> adapter_attachments = new ArrayList<>();

                String document_64 = Utils.ENCODE_TO_BASE64(file.getBytes());

                com.vnpt.xml.ed.Ed edoc = EdXML.readDocument(file.getInputStream());
                com.vnpt.xml.base.header.Header header = edoc.getHeader();
                if (header != null) {
                    com.vnpt.xml.ed.header.MessageHeader messageHeader = (com.vnpt.xml.ed.header.MessageHeader) header
                            .getMessageHeader();
                    if (messageHeader != null) {
                        com.vnpt.xml.ed.header.Code code = messageHeader.getCode();
                        if (code != null) {
                            code_number = code.getCodeNumber();
                            code_notation = code.getCodeNotation();
                        }
                        com.vnpt.xml.ed.header.DocumentType documentType = messageHeader.getDocumentType();
                        if (documentType != null) {
                            document_type_type = documentType.getType();
                            document_type_name = documentType.getTypeName();
                            Optional<DocumentType> check_document_type = documentTypeService
                                    .findByTypeAndTypeName(document_type_type, document_type_name);
                            if (!check_document_type.isPresent()) {
                                return CustomResponse.Response_data(404, "Khong tim thay loai van ban");
                            }
                            document_type = check_document_type.get();
                        }
                        com.vnpt.xml.base.header.SignerInfo signerInfo = messageHeader.getSignerInfo();
                        if (signerInfo != null) {
                            signer_info_competence = signerInfo.getCompetence();
                            signer_info_position = signerInfo.getPosition();
                            signer_info_fullname = signerInfo.getFullName();
                        }
                        com.vnpt.xml.ed.header.OtherInfo otherInfo = messageHeader.getOtherInfo();
                        if (otherInfo != null) {
                            other_info_priority = otherInfo.getPriority();
                            other_info_sphere_of_promulgation = otherInfo.getSphereOfPromulgation();
                            other_info_typer_notation = otherInfo.getTyperNotation();
                            other_info_promulgation_amount = otherInfo.getPromulgationAmount();
                            other_info_page_amount = otherInfo.getPageAmount();
                            appendixes = otherInfo.getAppendixes();
                        }
                        PromulgationInfo promulgationInfo = messageHeader.getPromulgationInfo();
                        if (promulgationInfo != null) {
                            promulgation_place = promulgationInfo.getPlace();
                            promulgation_date = Utils.DATE_TO_YYYY_MM_DD(promulgationInfo.getPromulgationDate());
                        }
                        subject = messageHeader.getSubject();
                        content = messageHeader.getContent();
                        due_date = Utils.DATE_TO_YYYY_MM_DD(messageHeader.getDueDate());
                        to_places = messageHeader.getToPlaces();
                        steering_type = messageHeader.getSteeringType();
                        docIdEdxml = messageHeader.getDocumentId();
                        com.vnpt.xml.base.header.Organization fromEdXML = messageHeader.getFrom();
                        if (fromEdXML != null) {
                            Optional<Organization> checkFrom = organizationService
                                    .findByOrganId(fromEdXML.getOrganId());
                            if (!checkFrom.isPresent()) {
                                return CustomResponse.Response_data(404, "Khong tim thay don vi");
                            }
                            from = checkFrom.get();
                        }
                        List<com.vnpt.xml.base.header.Organization> toesEdXML = messageHeader.getToes();
                        if (toesEdXML != null && toesEdXML.size() > 0) {
                            com.vnpt.xml.base.header.Organization toEdXML = toesEdXML.get(0);
                            if (toEdXML != null) {
                                Optional<Organization> checkTo = organizationService
                                        .findByOrganId(toEdXML.getOrganId());
                                if (!checkTo.isPresent()) {
                                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                                }
                                to = checkTo.get();
                            }
                        }
                        if (messageHeader.getResponseFor() != null) {
                            ResponseFor responseFor = messageHeader.getResponseFor().get(0);
                            if (responseFor != null) {
                                response_for_organ_id = responseFor.getOrganId();
                                response_for_code = responseFor.getCode();
                                response_for_promulgation_date = Utils
                                        .DATE_TO_YYYY_MM_DD(responseFor.getPromulgationDate());
                                response_for_document_id = responseFor.getDocumentId();
                            }
                        }
                    }
                    com.vnpt.xml.base.header.TraceHeaderList traceHeaderList = header.getTraceHeaderList();
                    if (traceHeaderList != null) {
                        com.vnpt.xml.base.header.Bussiness business = traceHeaderList.getBussiness();
                        if (business != null) {
                            if (business.getBussinessDocumentInfo() != null
                                    && business.getBussinessDocumentInfo().get(0) != null) {
                                com.vnpt.xml.base.header.BussinessDocumentInfo bussinessDocumentInfo = business
                                        .getBussinessDocumentInfo().get(0);
                                if (bussinessDocumentInfo.getdocumentInfo() != null) {
                                    business_bussiness_document_info_document_info = Integer
                                            .parseInt(bussinessDocumentInfo.getdocumentInfo());
                                }
                                if (bussinessDocumentInfo.getdocumentReceiver() != null) {
                                    business_bussiness_document_info_document_receiver = Integer
                                            .parseInt(bussinessDocumentInfo.getdocumentReceiver());
                                }
                                if (bussinessDocumentInfo.getreceiverList() != null &&
                                        bussinessDocumentInfo.getreceiverList().get(0) != null &&
                                        bussinessDocumentInfo.getreceiverList().get(0).getReceiver() != null) {
                                    List<com.vnpt.xml.base.header.Receiver> business_bussiness_document_info_receiver_edxml_list = bussinessDocumentInfo
                                            .getreceiverList().get(0).getReceiver();
                                    for (com.vnpt.xml.base.header.Receiver business_bussiness_document_info_receiver_edxml : business_bussiness_document_info_receiver_edxml_list) {
                                        if (business_bussiness_document_info_receiver_edxml.getReceiverType() != null) {
                                            UpdateReceiver updateReceiver = new UpdateReceiver(
                                                    Integer.parseInt(business_bussiness_document_info_receiver_edxml
                                                            .getReceiverType()),
                                                    business_bussiness_document_info_receiver_edxml.getOrganId());
                                            business_bussiness_document_info_receiver_list.add(updateReceiver);
                                        }
                                    }
                                }
                            }

                            com.vnpt.xml.base.header.StaffInfo staffInfo = business.getStaffInfo().get(0);
                            if (staffInfo != null) {
                                business_staff_info_department = staffInfo.getDepartment();
                                business_staff_info_staff = staffInfo.getStaff();
                                business_staff_info_mobile = staffInfo.getMobile();
                                business_staff_info_email = staffInfo.getEmail();
                            }
                            if (business.getBussinessDocType() != null) {
                                business_bussiness_doc_type = Integer.parseInt(business.getBussinessDocType());
                            }
                            business_bussiness_doc_reason = business.getBussinessDocReason();
                            business_document_id = business.getDocumentId();
                            if (business.getPaper() != null) {
                                business_paper = Integer.parseInt(business.getPaper());
                            }
                            if (business.getReplacementInfoList() != null
                                    && business.getReplacementInfoList().get(0) != null) {
                                List<com.vnpt.xml.base.header.ReplacementInfo> business_replacement_info_edxml_list = business
                                        .getReplacementInfoList().get(0).getReplacementInfo();
                                if (business.getReplacementInfoList().get(0).getReplacementInfo() != null) {
                                    for (com.vnpt.xml.base.header.ReplacementInfo business_replacement_info_edxml : business_replacement_info_edxml_list) {
                                        ReplacementInfo replacementInfo = new ReplacementInfo();
                                        replacementInfo
                                                .setReplacementInfoDocumentId(
                                                        business_replacement_info_edxml.getDocumentId());
                                        if (business_replacement_info_edxml.getOrganIdList() != null) {
                                            List<String> replacement_info_organ_id_list = business_replacement_info_edxml
                                                    .getOrganIdList()
                                                    .getOrganId();
                                            replacementInfo
                                                    .setReplacementInfoOrganIdList(replacement_info_organ_id_list);
                                            business_replacement_info_list.add(replacementInfo);
                                        }
                                    }
                                }
                            }
                        }
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
                }

                List<com.vnpt.xml.base.attachment.Attachment> edoc_attachments = edoc.getAttachments();
                if (edoc_attachments != null) {
                    for (com.vnpt.xml.base.attachment.Attachment edoc_attachment : edoc_attachments) {
                        Attachment adapter_attachment = new Attachment(
                                edoc_attachment.getContentType(),
                                edoc_attachment.getContentId(),
                                edoc_attachment.getDescription(),
                                edoc_attachment.getContentTransferEncoded(),
                                edoc_attachment.getName());
                        adapter_attachments.add(adapter_attachment);
                    }
                }

                Document document = new Document(
                        Utils.UUID(),
                        from,
                        to,
                        code_number,
                        code_notation,
                        promulgation_place,
                        promulgation_date,
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
                        response_for_organ_id,
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
                        null,
                        receiverDocId,
                        Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.maTrangThai(),
                        Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.moTaTrangThai(),
                        Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.maTrangThai(),
                        Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.maTrangThai(),
                        Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.moTaTrangThaiGui(),
                        Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.moTaTrangThaiNhan(),
                        document_64);

                documentService.saveDocument(document);
                return CustomResponse.Response_no_data(201);
            } else {
                return CustomResponse.Response_data(400, "Thieu file edxml");

            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
