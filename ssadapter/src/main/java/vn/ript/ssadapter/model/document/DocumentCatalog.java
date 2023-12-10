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
@Table(name = "_documentcatalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class DocumentCatalog {

	@Id
	private String id;

	// ============================ Header ============================
	// ++++++++++++++++++++++++++++ MessageHeader ++++++++++++++++++++++++++++

	// 1.1. From
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_fromId", referencedColumnName = "id")
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	private Organization from;

	// 1.3. Code
	// 1.3.1. CodeNumber
	@Column(name = "_codeCodeNumber", columnDefinition = "text")
	private String codeCodeNumber;

	// 1.3.2. CodeNotation
	@Column(name = "_codeCodeNotation", columnDefinition = "text")
	private String codeCodeNotation;

	// 1.4. PromulgationInfo
	// 1.4.1. Place
	@Column(name = "_promulgationInfoPlace", columnDefinition = "text")
	private String promulgationInfoPlace;

	// 1.4.2. PromulgationDate
	@Column(name = "_promulgationInfoPromulgationDate", columnDefinition = "text")
	private String promulgationInfoPromulgationDate;

	// 1.5. DocumentType
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_documentTypeId", referencedColumnName = "id")
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
	@Column(name = "_signerInfoCompetence", columnDefinition = "text")
	private String signerInfoCompetence;

	// 1.8.2. Position
	@Column(name = "_signerInfoPosition", columnDefinition = "text")
	private String signerInfoPosition;

	// 1.8.1. FullName
	@Column(name = "_signerInfoFullName", columnDefinition = "text")
	private String signerInfoFullName;

	// 1.10. ToPlaces
	@ElementCollection
	private List<String> toPlaces;

	// 1.11.2. SphereOfPromulgation
	@Column(name = "_otherInfoSphereOfPromulgation", columnDefinition = "text")
	private String otherInfoSphereOfPromulgation;

	// 1.11.3. TyperNotation
	@Column(name = "_otherInfoTyperNotation", columnDefinition = "text")
	private String otherInfoTyperNotation;

	// 1.11.5. PageAmount
	@Column(name = "_otherInfoPageAmount")
	private Integer otherInfoPageAmount;

	// 1.11.6. Appendixes
	@ElementCollection
	private List<String> appendixes;

	// 1.13. SteeringType
	@Column(name = "_steeringType")
	private Integer steeringType;

	// 1.14. DocumentId
	@Column(name = "_documentId", columnDefinition = "text")
	private String documentId;

	// ++++++++++++++++++++++++++++ End MessageHeader ++++++++++++++++++++++++++++
	// ++++++++++++++++++++++++++++ TraceHeaderList ++++++++++++++++++++++++++++

	// 2.2. Business
	// 2.2.1. BusinessDocType
	@Column(name = "_businessBussinessDocType")
	private Integer businessBussinessDocType;

	// 2.2.2. BusinessDocReason
	@Column(name = "_businessBussinessDocReason", columnDefinition = "text")
	private String businessBussinessDocReason;

	// 2.2.3. BusinessDocumentInfo
	// 2.2.3.1. DocumentInfo
	@Column(name = "_businessBussinessDocumentInfoDocumentInfo", columnDefinition = "text")
	private Integer businessBussinessDocumentInfoDocumentInfo;

	// 2.2.4. DocumentId
	@Column(name = "_businessDocumentId", columnDefinition = "text")
	private String businessDocumentId;

	// 2.2.5. StaffInfo
	// 2.2.5.1. Department
	@Column(name = "_businessStaffInfoDepartment", columnDefinition = "text")
	private String businessStaffInfoDepartment;

	// 2.2.5.2. Staff
	@Column(name = "_businessStaffInfoStaff", columnDefinition = "text")
	private String businessStaffInfoStaff;

	// 2.2.5.3. Mobile
	@Column(name = "_businessStaffInfoMobile", columnDefinition = "text")
	private String businessStaffInfoMobile;

	// 2.2.5.4. Email
	@Column(name = "_businessStaffInfoEmail", columnDefinition = "text")
	private String businessStaffInfoEmail;

	// 2.2.8. ReplacementInfoList
	@ElementCollection
	private List<String> businessReplacementInfoDocumentIdList;

	// ++++++++++++++++++++++++++++ End TraceHeaderList ++++++++++++++++++++++++++++
	// ============================ End Header ============================
	// ============================ AttachmentEncoded ============================

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "_attachments")
	private List<Attachment> attachments;

	// ============================ End AttachmentEncoded ============================
	// ============================ Addition ============================

	@Column(name = "_senderDocId", columnDefinition = "text")
	private String senderDocId;

	@Column(name = "_receiverDocId", columnDefinition = "text")
	private String receiverDocId;

	@Column(name = "_status", columnDefinition = "text")
	private String status;

	@Column(name = "_statusDesc", columnDefinition = "text")
	private String statusDesc;

	@Column(name = "_data", columnDefinition = "text")
	private String data;

	@Column(name = "_isPublic")
	private Boolean isPublic;

	@OneToMany(cascade = CascadeType.ALL)
	List<Organization> allowedOrganizations;
	
	// ============================ End Addition ============================
	
}
