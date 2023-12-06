package vn.ript.ssadapter.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vnpt.xml.base.Content;
import com.vnpt.xml.base.builder.BuildException;
import com.vnpt.xml.base.header.Bussiness;
import com.vnpt.xml.base.header.BussinessDocumentInfo;
import com.vnpt.xml.base.header.Header;
import com.vnpt.xml.base.header.KeyInfo;
import com.vnpt.xml.base.header.OrganIdList;
import com.vnpt.xml.base.header.Organization;
import com.vnpt.xml.base.header.Receiver;
import com.vnpt.xml.base.header.ReceiverList;
import com.vnpt.xml.base.header.ReplacementInfo;
import com.vnpt.xml.base.header.ReplacementInfoList;
import com.vnpt.xml.base.header.ResponseFor;
import com.vnpt.xml.base.header.SignReference;
import com.vnpt.xml.base.header.Signature;
import com.vnpt.xml.base.header.SignedInfo;
import com.vnpt.xml.base.header.SignerInfo;
import com.vnpt.xml.base.header.StaffInfo;
import com.vnpt.xml.base.header.TraceHeader;
import com.vnpt.xml.base.header.TraceHeaderList;
import com.vnpt.xml.base.header.X509Data;
import com.vnpt.xml.base.util.DateUtils;
import com.vnpt.xml.ed.Ed;
import com.vnpt.xml.ed.builder.EdXmlBuilder;
import com.vnpt.xml.ed.header.Code;
import com.vnpt.xml.ed.header.DocumentType;
import com.vnpt.xml.ed.header.MessageHeader;
import com.vnpt.xml.ed.header.OtherInfo;
import com.vnpt.xml.ed.header.PromulgationInfo;
import com.vnpt.xml.status.Status;
import com.vnpt.xml.status.builder.StatusXmlBuilder;
import com.vnpt.xml.status.header.MessageStatus;

public class EdXMLBuild {
	public static Content createEdoc_new(
			vn.ript.ssadapter.model.Organization from,
			List<vn.ript.ssadapter.model.Organization> to_list,
			String code_number,
			String code_notation,
			String promulgation_place,
			vn.ript.ssadapter.model.document.DocumentType document_type,
			String subject,
			String content,
			String signer_info_competence,
			String signer_info_position,
			String signer_info_fullname,
			String due_date,
			List<String> to_places,
			Integer other_info_priority,
			String other_info_sphere_of_promulgation,
			String other_info_typer_notation,
			Integer other_info_promulgation_amount,
			Integer other_info_page_amount,
			List<String> appendixes,
			Boolean response_for,
			Integer steering_type,
			Integer business_bussiness_doc_type,
			String business_bussiness_doc_reason,
			Integer business_bussiness_document_info_document_info,
			Integer business_bussiness_document_info_document_receiver,
			List<vn.ript.ssadapter.model.document.UpdateReceiver> business_bussiness_document_info_receiver_list,
			String business_document_id,
			String business_staff_info_department,
			String business_staff_info_staff,
			String business_staff_info_mobile,
			String business_staff_info_email,
			Integer business_paper,
			List<vn.ript.ssadapter.model.document.ReplacementInfo> business_replacement_info_list,
			List<File> attachments,
			List<String> attachment_description_list) throws Exception {

		String date = Utils.DATE_NOW();

		Ed ed = new Ed();

		// khoi tao code cho van ban
		Code code = new Code(code_number, code_notation);

		// khoi tao don vi gui
		Organization orgFrom = new Organization(
				from.getOrganId(),
				from.getOrganizationInCharge(),
				from.getOrganName(),
				from.getOrganAdd(),
				from.getEmail(),
				from.getTelephone(),
				from.getFax(),
				from.getWebsite());

		// khoi tao danh sach don vi nhan
		List<Organization> orgToes = new ArrayList<>();
		for (vn.ript.ssadapter.model.Organization to : to_list) {
			orgToes.add(new Organization(
					to.getOrganId(),
					to.getOrganizationInCharge(),
					to.getOrganName(),
					to.getOrganAdd(),
					to.getEmail(),
					to.getTelephone(),
					to.getFax(),
					to.getWebsite()));
		}

		// khoi tao thong tin ban hanh
		PromulgationInfo promulgationInfo = new PromulgationInfo(promulgation_place, DateUtils.parse(date));

		// khoi tao loai van ban
		DocumentType documentType = new DocumentType(document_type.getType(), document_type.getTypeName());

		// khoi tao thong tin nguoi ky
		SignerInfo signerInfo = new SignerInfo(signer_info_competence, signer_info_position, signer_info_fullname);

		// khoi tao header
		MessageHeader header = new MessageHeader(orgFrom, orgToes, code, promulgationInfo, documentType, subject,
				content, null, signerInfo, DateUtils.parse(due_date), null, null);

		// dia diem noi nhan va luu van ban
		for (String to_place : to_places) {
			header.addToPlace(to_place);
		}

		// khoi tao loai chi dao
		header.setSteeringType(steering_type);

		// khoi tao cac thong tin khac cua van ban
		OtherInfo otherInfo = new OtherInfo();
		otherInfo.setPriority(other_info_priority);
		otherInfo.setPromulgationAmount(other_info_promulgation_amount);
		otherInfo.setPageAmount(other_info_page_amount);
		otherInfo.setSphereOfPromulgation(other_info_sphere_of_promulgation);
		otherInfo.setTyperNotation(other_info_typer_notation);
		otherInfo.setAppendixes(appendixes);
		header.setOtherInfo(otherInfo);

		String document_id = from.getOrganId() + "," + date + "," + code_number + "/" + code_notation;
		// khoi tao thong tin ma dinh danh cua van ban
		header.setDocumentId(document_id);

		// khoi tao thong tin van ban phan hoi va phuc dap
		// List<ResponseFor> response_for_list = new
		if (response_for != null && response_for == true) {
			for (vn.ript.ssadapter.model.Organization to : to_list) {
				ResponseFor responseFor = new ResponseFor(to.getOrganId(), code_number + "/" + code_notation,
						DateUtils.parse(date), document_id);
				header.addResponseFor(responseFor);
			}
		}

		// khoi tao cac thong tin traceHeader
		TraceHeaderList trList = new TraceHeaderList();
		TraceHeader traceFrom = new TraceHeader();
		traceFrom.setOrganId(from.getOrganId());
		traceFrom.setTimestamp(DateUtils.parse(date));
		trList.addTraceHeader(traceFrom);

		// khoi tao danh sach cac truong thong tin duoc van ban cap nhat
		BussinessDocumentInfo bussinessDocumentInfo = new BussinessDocumentInfo();
		bussinessDocumentInfo.setdocumentInfo(business_bussiness_document_info_document_info.toString());
		bussinessDocumentInfo.setdocumentReceiver(business_bussiness_document_info_document_receiver.toString());

		// khoi tao danh sach cac don vi nhan bi thay doi khi cap nhat van ban
		ReceiverList receiverList = new ReceiverList();
		for (vn.ript.ssadapter.model.document.UpdateReceiver business_bussiness_document_info_receiver : business_bussiness_document_info_receiver_list) {
			Receiver receiver = new Receiver(business_bussiness_document_info_receiver.getUpdateReceiver_OrganId(),
					business_bussiness_document_info_receiver.getUpdateReceiver_ReceiverType().toString());
			receiverList.addReceiver(receiver);
		}

		// khoi tao thong tin ve nguoi xu ly
		StaffInfo staffInfo = new StaffInfo();
		staffInfo.setDepartment(business_staff_info_department);
		staffInfo.setStaff(business_staff_info_staff);
		staffInfo.setEmail(business_staff_info_email);
		staffInfo.setMobile(business_staff_info_mobile);

		// khoi tao thong tin danh sach van ban bi thay the
		ReplacementInfoList replacementInfoList = new ReplacementInfoList();
		for (vn.ript.ssadapter.model.document.ReplacementInfo business_replacement_info : business_replacement_info_list) {
			OrganIdList organIdList = new OrganIdList();
			organIdList.setOrganId(business_replacement_info.getReplacementInfo_OrganIdList());
			ReplacementInfo replacementInfo = new ReplacementInfo(
					business_replacement_info.getReplacementInfo_DocumentId(),
					organIdList);
			replacementInfoList.addReplacementInfo(replacementInfo);
		}

		// khoi tao cac thong tin danh dau loai nghiep vu van ban
		Bussiness bussiness = new Bussiness();
		bussiness.setBussinessDocReason(business_bussiness_doc_reason);
		bussiness.setBussinessDocType(business_bussiness_doc_type.toString());
		bussiness.setDocumentId(business_document_id);
		bussiness.setPaper(business_paper.toString());
		// add addreceiverList
		bussinessDocumentInfo.addreceiverList(receiverList);
		// add staffInfo
		bussiness.addStaffInfo(staffInfo);
		// add BussinessDocumentInfo
		bussiness.addBussinessDocumentInfo(bussinessDocumentInfo);
		// add ReplacementInfoList
		bussiness.addReplacementInfoList(replacementInfoList);

		// add TraceHeaderList
		trList.setBussiness(bussiness);

		// khoi tao thong tin chu ky
		SignReference signReference = new SignReference("", "http://www.w3.org/2000/09/xmldsig#sha1",
				"FwgIqsSYJshUS2+wlOM61L+q7Aw=");
		signReference.addToTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
		signReference.addToTransform("http://www.w3.org/TR/xml-exc-c14n#");
		List<SignReference> listSignReference = new ArrayList<SignReference>();
		listSignReference.add(signReference);
		SignedInfo signedInfo = new SignedInfo("http://www.w3.org/TR/2001/REC-xml-c14n-20010315",
				"http://www.w3.org/2000/09/xmldsig#rsa-sha1", listSignReference);
		String SignatureValue = "GbVb8n9+qFxz466Ag0sbpVdLPs2R0+9JBekp12UyarAZjoG0W/kPZZJ1auRZDcNcrSwzgkQMpqrjwxcy3ejzbY/ON5USUPgoYNYM8p4wsgQpEAeQaZ+EWLkkEBxYjb+iWEiAjYuJI7gtJoOyENcOK4fO050SXp2ctOc32LJMA5eEI6Hw7sxhc2LAgcPiynJHdDW2Z+eut6QZiUsbIF9+S3T6u/tfLImw39dgSlCxupwLPHepxuiLOqyd08HeJGCZufg9WqRBVyLFM76uCIaPRP5wwAdx72GVjPcG2kh+2jjrt7fqtJOufJzCObtQgPgBqIvDiZCGoOM41OQMiqtF3w==";
		KeyInfo keyInfo = new KeyInfo(new X509Data("CN=user05", "MIIFqTCCBJG"));
		Signature signature = new Signature(signedInfo, SignatureValue, keyInfo);

		// khoi tao header van ban
		ed.setHeader(new Header(header, trList, signature));

		// khoi tao file dinh kem
		if (attachments != null) {
			for (int i = 0; i < attachments.size(); i++) {
				File attachment = attachments.get(i);
				String description = attachment_description_list.get(i);
				String fileName = attachment.getName();
				ed.addAttachment(new com.vnpt.xml.base.attachment.Attachment(
						Utils.SHA256_HASH(Files.readAllBytes(attachment.toPath()).toString()), fileName, description,
						attachment));
			}
		}
		// ghi file ra thu muc
		Content edXmlContent = EdXmlBuilder.build(ed, Utils.EDOC_DIR);

		return edXmlContent;
	}

	public static Content create_status(
			vn.ript.ssadapter.model.Organization from,
			vn.ript.ssadapter.model.document.Document document,
			String status_staff_info_department,
			String status_staff_info_staff,
			String status_staff_info_mobile,
			String status_staff_info_email,
			String status_status_code,
			String status_description) {
		// create header
		Header header = new Header();
		MessageStatus msgStatus = new MessageStatus();

		// set ResponseFor Tag
		msgStatus.setResponseFor(new ResponseFor(
				document.getFrom().getOrganId(),
				document.getCode_CodeNumber() + "/" + document.getCode_CodeNotation(),
				DateUtils.parse(document.getPromulgationInfo_PromulgationDate()),
				document.getDocumentId()));
		// set from information (organization)
		msgStatus.setFrom(new Organization(
				from.getOrganId(),
				from.getOrganizationInCharge(),
				from.getOrganName(),
				from.getOrganAdd(),
				from.getEmail(),
				from.getTelephone(),
				from.getFax(),
				from.getWebsite()));

		// set status code info
		msgStatus.setStatusCode(status_status_code);
		msgStatus.setDescription(status_description);
		msgStatus.setTimestamp(new Date());

		// set staff details
		StaffInfo staffInfo = new StaffInfo();
		staffInfo.setDepartment(status_staff_info_department);
		staffInfo.setStaff(status_staff_info_staff);
		staffInfo.setEmail(status_staff_info_email);
		staffInfo.setMobile(status_staff_info_mobile);
		msgStatus.setStaffInfo(staffInfo);
		header.setMessageHeader(msgStatus);

		// build status edxml

		Content content = null;
		try {
			content = StatusXmlBuilder.build(new Status(header), Utils.EDOC_DIR);
		} catch (BuildException e) {
			e.printStackTrace();
		}
		return content;
	}

}
