package vn.ript.ssadapter.model.document;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.ript.ssadapter.model.Organization;

@Entity
@Table(name = "_document")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class Document {

	@Id
	private String id;

	// ============================ Header ============================
	// ++++++++++++++++++++++++++++ MessageHeader ++++++++++++++++++++++++++++

	// 1.1. From
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_from_id", referencedColumnName = "id")
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	private Organization from;

	// 1.2. To
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_to_id", referencedColumnName = "id")
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	private Organization to;

	// 1.3. Code
	// 1.3.1. CodeNumber
	@Column(name = "_code_CodeNumber", columnDefinition = "text")
	private String code_CodeNumber;

	// 1.3.2. CodeNotation
	@Column(name = "_code_CodeNotation", columnDefinition = "text")
	private String code_CodeNotation;

	// 1.4. PromulgationInfo
	// 1.4.1. Place
	@Column(name = "_promulgationInfo_Place", columnDefinition = "text")
	private String promulgationInfo_Place;

	// 1.4.2. PromulgationDate
	@Column(name = "_promulgationInfo_PromulgationDate", columnDefinition = "text")
	private String promulgationInfo_PromulgationDate;

	// 1.5. DocumentType
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_documentType_id", referencedColumnName = "id")
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	private DocumentType documentType;

	// 1.6. Subject
	@Column(name = "_subject", columnDefinition = "text")
	private String subject;

	// 1.7. Content
	@Column(name = "_content", columnDefinition = "text")
	private String content;

	// 1.8. SignerInfo
	// 1.8.1. Competence
	@Column(name = "_signerInfo_Competence", columnDefinition = "text")
	private String signerInfo_Competence;

	// 1.8.2. Position
	@Column(name = "_signerInfo_Position", columnDefinition = "text")
	private String signerInfo_Position;

	// 1.8.1. FullName
	@Column(name = "_signerInfo_FullName", columnDefinition = "text")
	private String signerInfo_FullName;

	// 1.9. DueDate
	@Column(name = "_dueDate", columnDefinition = "text")
	private String dueDate;

	// 1.10. ToPlaces
	@ElementCollection
	private List<String> toPlaces;

	// 1.11. OtherInfo
	// 1.11.1. Priority
	@Column(name = "_otherInfo_Priority")
	private Integer otherInfo_Priority;

	// 1.11.2. SphereOfPromulgation
	@Column(name = "_otherInfo_SphereOfPromulgation", columnDefinition = "text")
	private String otherInfo_SphereOfPromulgation;

	// 1.11.3. TyperNotation
	@Column(name = "_otherInfo_TyperNotation", columnDefinition = "text")
	private String otherInfo_TyperNotation;

	// 1.11.4. PromulgationAmount
	@Column(name = "_otherInfo_PromulgationAmount")
	private Integer otherInfo_PromulgationAmount;

	// 1.11.5. PageAmount
	@Column(name = "_otherInfo_PageAmount")
	private Integer otherInfo_PageAmount;

	// 1.11.6. Appendixes
	@ElementCollection
	private List<String> appendixes;

	// 1.12. ResponseFor
	// 1.12.1. OrganId
	@Column(name = "_responseFor_OrganId", columnDefinition = "text")
	private String responseFor_OrganId;

	// 1.12.2. Code
	@Column(name = "_responseFor_Code", columnDefinition = "text")
	private String responseFor_Code;

	// 1.12.3. PromulgationDate
	@Column(name = "_responseFor_PromulgationDate", columnDefinition = "text")
	private String responseFor_PromulgationDate;

	// 1.12.4. DocumentId
	@Column(name = "_responseFor_DocumentId", columnDefinition = "text")
	private String responseFor_DocumentId;

	// 1.13. SteeringType
	@Column(name = "_steeringType")
	private Integer steeringType;

	// 1.14. DocumentId
	@Column(name = "_documentId", columnDefinition = "text")
	private String documentId;
	
	// Status StatusCode
	@Column(name = "_statusCode", columnDefinition = "text")
	private String statusCode;
	
	// Status Description
	@Column(name = "_description", columnDefinition = "text")
	private String description;
	
	// Status Timestamp
	@Column(name = "_timestamp", columnDefinition = "text")
	private String timestamp;

	// ++++++++++++++++++++++++++++ End MessageHeader ++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++ TraceHeaderList ++++++++++++++++++++++++++++

	// 2.1. TraceHeader
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "_traceHeaders")
	private List<TraceHeader> traceHeaders;

	// 2.2. Business
	// 2.2.1. BusinessDocType
	@Column(name = "_business_BussinessDocType")
	private Integer business_BussinessDocType;

	// 2.2.2. BusinessDocReason
	@Column(name = "_business_BussinessDocReason", columnDefinition = "text")
	private String business_BussinessDocReason;

	// 2.2.3. BusinessDocumentInfo
	// 2.2.3.1. DocumentInfo
	@Column(name = "_business_BussinessDocumentInfo_DocumentInfo", columnDefinition = "text")
	private Integer business_BussinessDocumentInfo_DocumentInfo;

	// 2.2.3.2. DocumentReceiver
	@Column(name = "_business_BussinessDocumentInfo_DocumentReceiver", columnDefinition = "text")
	private Integer business_BussinessDocumentInfo_DocumentReceiver;

	// 2.2.3.3. ReceiverList
	// @OneToMany(mappedBy = "traceHeaderList")
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "_business_BussinessDocumentInfo_ReceiverList")
	List<UpdateReceiver> business_BussinessDocumentInfo_ReceiverList;

	// 2.2.4. DocumentId
	@Column(name = "_business_DocumentId", columnDefinition = "text")
	private String business_DocumentId;

	// 2.2.5. StaffInfo
	// 2.2.5.1. Department
	@Column(name = "_business_StaffInfo_Department", columnDefinition = "text")
	private String business_StaffInfo_Department;

	// 2.2.5.2. Staff
	@Column(name = "_business_StaffInfo_Staff", columnDefinition = "text")
	private String business_StaffInfo_Staff;

	// 2.2.5.3. Mobile
	@Column(name = "_business_StaffInfo_Mobile", columnDefinition = "text")
	private String business_StaffInfo_Mobile;

	// 2.2.5.4. Email
	@Column(name = "_business_StaffInfo_Email", columnDefinition = "text")
	private String business_StaffInfo_Email;

	// 2.2.6. Paper
	@Column(name = "_business_Paper")
	private Integer business_Paper;

	// 2.2.8. ReplacementInfoList
	@OneToMany(cascade = CascadeType.ALL)
	private List<ReplacementInfo> business_ReplacementInfoList;

	// ++++++++++++++++++++++++++++ End TraceHeaderList ++++++++++++++++++++++++++++
	// ============================ End Header ============================
	// ============================ AttachmentEncoded ============================

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "_attachments")
	private List<Attachment> attachments;

	// ============================ End AttachmentEncoded ============================
	// ============================ Addition ============================
	
	@Column(name = "_serviceType", columnDefinition = "text")
	private String serviceType;

	@Column(name = "_messageType", columnDefinition = "text")
	private String messageType;

	@Column(name = "_senderDocId", columnDefinition = "text")
	private String senderDocId;

	@Column(name = "_receiverDocId", columnDefinition = "text")
	private String receiverDocId;

	@Column(name = "_status", columnDefinition = "text")
	private String status;
	@Column(name = "_statusDesc", columnDefinition = "text")
	private String statusDesc;
	@Column(name = "_sendStatus", columnDefinition = "text")
	private String sendStatus;

	@Column(name = "_receiveStatus", columnDefinition = "text")
	private String receiveStatus;

	@Column(name = "_sendStatusDesc", columnDefinition = "text")
	private String sendStatusDesc;

	@Column(name = "_receiveStatusDesc", columnDefinition = "text")
	private String receiveStatusDesc;
	
	@Column(name = "_data", columnDefinition = "text")
	private String data;
	
	// ============================ End Addition ============================
	
}
