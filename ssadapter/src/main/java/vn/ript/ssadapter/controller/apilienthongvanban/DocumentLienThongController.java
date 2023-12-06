package vn.ript.ssadapter.controller.apilienthongvanban;

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

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.document.Document;
import vn.ript.ssadapter.model.document.DocumentType;
import vn.ript.ssadapter.model.document.ReplacementInfo;
import vn.ript.ssadapter.model.document.UpdateReceiver;
import vn.ript.ssadapter.service.DocumentService;
import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXML;
import vn.ript.ssadapter.utils.Utils;

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
    @PostMapping(value = "/new")
    public ResponseEntity<Map<String, Object>> sendDocument(
            @RequestPart(name = "file", required = true) MultipartFile file) {
        try {
            String receiverDocId = Utils.UUID();
            if (!file.isEmpty()) {
                com.vnpt.xml.ed.Ed edoc = EdXML.readDocument(file.getInputStream());
                com.vnpt.xml.base.header.Header header = edoc.getHeader();
                com.vnpt.xml.ed.header.MessageHeader messageHeader = (com.vnpt.xml.ed.header.MessageHeader) header
                        .getMessageHeader();
                com.vnpt.xml.base.header.TraceHeaderList traceHeaderList = header.getTraceHeaderList();
                com.vnpt.xml.ed.header.Code code = messageHeader.getCode();
                com.vnpt.xml.ed.header.DocumentType documentType = messageHeader.getDocumentType();
                com.vnpt.xml.base.header.SignerInfo signerInfo = messageHeader.getSignerInfo();
                com.vnpt.xml.ed.header.OtherInfo otherInfo = messageHeader.getOtherInfo();
                com.vnpt.xml.base.header.Bussiness business = traceHeaderList.getBussiness();
                com.vnpt.xml.base.header.BussinessDocumentInfo bussinessDocumentInfo = traceHeaderList.getBussiness()
                        .getBussinessDocumentInfo()
                        .get(0);
                com.vnpt.xml.base.header.StaffInfo staffInfo = business.getStaffInfo().get(0);

                String code_number = code.getCodeNumber();
                String code_notation = code.getCodeNotation();
                String promulgation_place = messageHeader.getPromulgationInfo().getPlace();
                Integer document_type_type = documentType.getType();
                String document_type_name = documentType.getTypeName();
                String subject = messageHeader.getSubject();
                String content = messageHeader.getContent();
                String signer_info_competence = signerInfo.getCompetence();
                String signer_info_position = signerInfo.getPosition();
                String signer_info_fullname = signerInfo.getFullName();
                String due_date = Utils.date_to_yyyy_mm_dd(messageHeader.getDueDate());
                List<String> to_places = messageHeader.getToPlaces();
                Integer other_info_priority = otherInfo.getPriority();
                String other_info_sphere_of_promulgation = otherInfo.getSphereOfPromulgation();
                String other_info_typer_notation = otherInfo.getTyperNotation();
                Integer other_info_promulgation_amount = otherInfo.getPromulgationAmount();
                Integer other_info_page_amount = otherInfo.getPageAmount();
                List<String> appendixes = otherInfo.getAppendixes();
                Integer steering_type = messageHeader.getSteeringType();
                String docIdEdxml = messageHeader.getDocumentId();
                Integer business_bussiness_doc_type = Integer.parseInt(business.getBussinessDocType());
                String business_bussiness_doc_reason = business.getBussinessDocReason();
                Integer business_bussiness_document_info_document_info = Integer
                        .parseInt(bussinessDocumentInfo.getdocumentInfo());
                Integer business_bussiness_document_info_document_receiver = Integer
                        .parseInt(bussinessDocumentInfo.getdocumentReceiver());
                String business_document_id = business.getDocumentId();
                String business_staff_info_department = staffInfo.getDepartment();
                String business_staff_info_staff = staffInfo.getStaff();
                String business_staff_info_mobile = staffInfo.getMobile();
                String business_staff_info_email = staffInfo.getEmail();
                Integer business_paper = Integer.parseInt(business.getPaper());
                List<com.vnpt.xml.base.header.Receiver> business_bussiness_document_info_receiver_edxml_list = bussinessDocumentInfo
                        .getreceiverList().get(0).getReceiver();
                List<com.vnpt.xml.base.header.ReplacementInfo> business_replacement_info_edxml_list = business
                        .getReplacementInfoList().get(0).getReplacementInfo();

                String document_64 = Utils.ENCODE_TO_BASE64(file.getBytes());

                com.vnpt.xml.base.header.Organization fromEdXML = messageHeader.getFrom();
                Optional<Organization> checkFrom = organizationService.findByOrganId(fromEdXML.getOrganId());
                if (!checkFrom.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                }
                Organization from = checkFrom.get();

                Optional<Organization> checkTo = organizationService.findByOrganId(Utils.SS_ID);
                if (!checkTo.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                }

                Optional<DocumentType> check_document_type = documentTypeService
                        .findByTypeAndTypeName(document_type_type, document_type_name);
                if (!check_document_type.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay loai van ban");
                }

                List<UpdateReceiver> business_bussiness_document_info_receiver_list = new ArrayList<>();
                for (com.vnpt.xml.base.header.Receiver business_bussiness_document_info_receiver_edxml : business_bussiness_document_info_receiver_edxml_list) {
                    UpdateReceiver updateReceiver = new UpdateReceiver(
                            Integer.parseInt(business_bussiness_document_info_receiver_edxml.getReceiverType()),
                            business_bussiness_document_info_receiver_edxml.getOrganId());
                    business_bussiness_document_info_receiver_list.add(updateReceiver);
                }

                List<ReplacementInfo> business_replacement_info_list = new ArrayList<>();
                for (com.vnpt.xml.base.header.ReplacementInfo business_replacement_info_edxml : business_replacement_info_edxml_list) {
                    ReplacementInfo replacementInfo = new ReplacementInfo();
                    replacementInfo
                            .setReplacementInfo_DocumentId(business_replacement_info_edxml.getDocumentId());
                    List<String> replacement_info_organ_id_list = business_replacement_info_edxml.getOrganIdList()
                            .getOrganId();
                    replacementInfo.setReplacementInfo_OrganIdList(replacement_info_organ_id_list);
                    business_replacement_info_list.add(replacementInfo);
                }

                DocumentType document_type = check_document_type.get();

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
