package vn.ript.ssadapter.controller.test;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.ript.ssadapter.service.DocumentService;
import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXML;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/lienthong/v1/test/document/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestController {

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
                System.out.println(receiverDocId);
                com.vnpt.xml.ed.Ed edoc = EdXML.readDocument(file.getInputStream());
                System.out.println(edoc);
                com.vnpt.xml.base.header.Header header = edoc.getHeader();
                System.out.println(header);
                com.vnpt.xml.ed.header.MessageHeader messageHeader = (com.vnpt.xml.ed.header.MessageHeader) header
                        .getMessageHeader();
                System.out.println(messageHeader);
                com.vnpt.xml.base.header.TraceHeaderList traceHeaderList = header.getTraceHeaderList();
                System.out.println(traceHeaderList);
                com.vnpt.xml.ed.header.Code code = messageHeader.getCode();
                System.out.println(code);
                com.vnpt.xml.ed.header.DocumentType documentType = messageHeader.getDocumentType();
                System.out.println(documentType);
                com.vnpt.xml.base.header.SignerInfo signerInfo = messageHeader.getSignerInfo();
                System.out.println(signerInfo);
                com.vnpt.xml.ed.header.OtherInfo otherInfo = messageHeader.getOtherInfo();
                System.out.println(otherInfo);
                com.vnpt.xml.base.header.Bussiness business = traceHeaderList.getBussiness();
                System.out.println(business);
                if (business.getBussinessDocumentInfo() != null) {
                    com.vnpt.xml.base.header.BussinessDocumentInfo bussinessDocumentInfo = business
                            .getBussinessDocumentInfo().get(0);
                    System.out.println(bussinessDocumentInfo);
                } else {
                    System.out.println("===================bussinessDocumentInfo");
                }
                if (business.getStaffInfo() != null) {
                    com.vnpt.xml.base.header.StaffInfo staffInfo = business.getStaffInfo().get(0);
                    System.out.println(staffInfo);
                } else {
                    System.out.println("===================staffInfo");
                }

                String code_number = code.getCodeNumber();
                System.out.println(code_number);
                String code_notation = code.getCodeNotation();
                System.out.println(code_notation);
                String promulgation_place = messageHeader.getPromulgationInfo().getPlace();
                System.out.println(promulgation_place);
                Integer document_type_type = documentType.getType();
                System.out.println(document_type_type);
                String document_type_name = documentType.getTypeName();
                System.out.println(document_type_name);
                String subject = messageHeader.getSubject();
                System.out.println(subject);
                String content = messageHeader.getContent();
                System.out.println(content);
                String signer_info_competence = signerInfo.getCompetence();
                System.out.println(signer_info_competence);
                String signer_info_position = signerInfo.getPosition();
                System.out.println(signer_info_position);
                String signer_info_fullname = signerInfo.getFullName();
                System.out.println(signer_info_fullname);
                if (messageHeader.getDueDate() != null) {
                    String due_date = Utils.DATE_TO_YYYY_MM_DD(messageHeader.getDueDate());
                    System.out.println(due_date);
                } else {
                    System.out.println("=====================due_date");
                }
                List<String> to_places = messageHeader.getToPlaces();
                System.out.println(to_places);
                Integer other_info_priority = otherInfo.getPriority();
                System.out.println(other_info_priority);
                String other_info_sphere_of_promulgation = otherInfo.getSphereOfPromulgation();
                System.out.println(other_info_sphere_of_promulgation);
                String other_info_typer_notation = otherInfo.getTyperNotation();
                System.out.println(other_info_typer_notation);
                Integer other_info_promulgation_amount = otherInfo.getPromulgationAmount();
                System.out.println(other_info_promulgation_amount);
                Integer other_info_page_amount = otherInfo.getPageAmount();
                System.out.println(other_info_page_amount);
                List<String> appendixes = otherInfo.getAppendixes();
                System.out.println(appendixes);
                Integer steering_type = messageHeader.getSteeringType();
                System.out.println(steering_type);
                String docIdEdxml = messageHeader.getDocumentId();
                System.out.println(docIdEdxml);
                Integer business_bussiness_doc_type = Integer.parseInt(business.getBussinessDocType());
                System.out.println(business_bussiness_doc_type);
                String business_bussiness_doc_reason = business.getBussinessDocReason();
                System.out.println(business_bussiness_doc_reason);
                // Integer business_bussiness_document_info_document_info = Integer
                // .parseInt(bussinessDocumentInfo.getdocumentInfo());
                // System.out.println(business_bussiness_document_info_document_info);
                // Integer business_bussiness_document_info_document_receiver = Integer
                // .parseInt(bussinessDocumentInfo.getdocumentReceiver());
                // System.out.println(business_bussiness_document_info_document_receiver);
                // String business_document_id = business.getDocumentId();
                // System.out.println(business_document_id);
                // String business_staff_info_department = staffInfo.getDepartment();
                // System.out.println(business_staff_info_department);
                // String business_staff_info_staff = staffInfo.getStaff();
                // System.out.println(business_staff_info_staff);
                // String business_staff_info_mobile = staffInfo.getMobile();
                // System.out.println(business_staff_info_mobile);
                // String business_staff_info_email = staffInfo.getEmail();
                // System.out.println(business_staff_info_email);
                // Integer business_paper = Integer.parseInt(business.getPaper());
                // System.out.println(business_paper);
                // List<com.vnpt.xml.base.header.Receiver>
                // business_bussiness_document_info_receiver_edxml_list = bussinessDocumentInfo
                // .getreceiverList().get(0).getReceiver();
                // System.out.println(business_bussiness_document_info_receiver_edxml_list);
                // List<com.vnpt.xml.base.header.ReplacementInfo>
                // business_replacement_info_edxml_list = business
                // .getReplacementInfoList().get(0).getReplacementInfo();
                // System.out.println(business_replacement_info_edxml_list);

                // String document_64 = Utils.ENCODE_TO_BASE64(file.getBytes());
                // System.out.println(document_64);

                // com.vnpt.xml.base.header.Organization fromEdXML = messageHeader.getFrom();
                // System.out.println(fromEdXML);
                // Optional<Organization> checkFrom =
                // organizationService.findByOrganId(fromEdXML.getOrganId());
                // System.out.println(checkFrom);
                // if (!checkFrom.isPresent()) {
                // // return CustomResponse.Response_data(404, "Khong tim thay don vi");
                // }
                // Organization from = checkFrom.get();
                // System.out.println(from);

                // Optional<Organization> checkTo =
                // organizationService.findByOrganId(Utils.SS_ID);
                // System.out.println(checkTo);
                // if (!checkTo.isPresent()) {
                // // return CustomResponse.Response_data(404, "Khong tim thay don vi");
                // }

                // Optional<DocumentType> check_document_type = documentTypeService
                // .findByTypeAndTypeName(document_type_type, document_type_name);
                // System.out.println(check_document_type);
                // if (!check_document_type.isPresent()) {
                // // return CustomResponse.Response_data(404, "Khong tim thay loai van ban");
                // }

                // List<UpdateReceiver> business_bussiness_document_info_receiver_list = new
                // ArrayList<>();
                // if (business_bussiness_document_info_receiver_edxml_list != null)
                // for (com.vnpt.xml.base.header.Receiver
                // business_bussiness_document_info_receiver_edxml :
                // business_bussiness_document_info_receiver_edxml_list) {
                // UpdateReceiver updateReceiver = new UpdateReceiver(
                // Integer.parseInt(business_bussiness_document_info_receiver_edxml.getReceiverType()),
                // business_bussiness_document_info_receiver_edxml.getOrganId());
                // business_bussiness_document_info_receiver_list.add(updateReceiver);
                // }
                // System.out.println(business_bussiness_document_info_receiver_list);

                // List<ReplacementInfo> business_replacement_info_list = new ArrayList<>();
                // for (com.vnpt.xml.base.header.ReplacementInfo business_replacement_info_edxml
                // : business_replacement_info_edxml_list) {
                // ReplacementInfo replacementInfo = new ReplacementInfo();
                // replacementInfo
                // .setReplacementInfoDocumentId(business_replacement_info_edxml.getDocumentId());
                // List<String> replacement_info_organ_id_list =
                // business_replacement_info_edxml.getOrganIdList()
                // .getOrganId();
                // replacementInfo.setReplacementInfoOrganIdList(replacement_info_organ_id_list);
                // business_replacement_info_list.add(replacementInfo);
                // }

                // System.out.println(business_replacement_info_list);
                // DocumentType document_type = check_document_type.get();
                // System.out.println(document_type);

                // System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++=");
                // System.out.println(Utils.UUID());
                // System.out.println(from);
                // System.out.println(checkTo.get());
                // System.out.println(code_number);
                // System.out.println(code_notation);
                // System.out.println(promulgation_place);
                // System.out.println(Utils.DATE_NOW());
                // System.out.println(document_type);
                // System.out.println(subject);
                // System.out.println(content);
                // System.out.println(signer_info_competence);
                // System.out.println(signer_info_position);
                // System.out.println(signer_info_fullname);
                // // System.out.println(due_date);
                // System.out.println(to_places);
                // System.out.println(other_info_priority);
                // System.out.println(other_info_sphere_of_promulgation);
                // System.out.println(other_info_typer_notation);
                // System.out.println(other_info_promulgation_amount);
                // System.out.println(other_info_page_amount);
                // System.out.println(appendixes);
                // System.out.println(steering_type);
                // System.out.println(docIdEdxml);
                // System.out.println(business_bussiness_doc_type);
                // System.out.println(business_bussiness_doc_reason);
                // System.out.println(business_bussiness_document_info_document_info);
                // System.out.println(business_bussiness_document_info_document_receiver);
                // System.out.println(business_bussiness_document_info_receiver_list);
                // System.out.println(business_document_id);
                // System.out.println(business_staff_info_department);
                // System.out.println(business_staff_info_staff);
                // System.out.println(business_staff_info_mobile);
                // System.out.println(business_staff_info_email);
                // System.out.println(business_paper);
                // System.out.println(business_replacement_info_list);
                // System.out.println(receiverDocId);
                // System.out.println(Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.maTrangThai());
                // System.out.println(Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.moTaTrangThai());
                // System.out.println(Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.maTrangThai());
                // System.out.println(Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.maTrangThai());
                // System.out.println(Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.moTaTrangThaiGui());
                // System.out.println(Constants.TRANG_THAI_VAN_BAN.CHO_TIEP_NHAN.moTaTrangThaiNhan());
                // System.out.println(document_64);

                return CustomResponse.Response_no_data(201);
            } else {
                return CustomResponse.Response_data(400, "Thieu file edxml");

            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
