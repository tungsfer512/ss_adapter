package vn.ript.base.model.document;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import vn.ript.base.model.Organization;

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

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_fromId", referencedColumnName = "id")
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	private Organization from;

	@Column(name = "_codeCodeNumber", columnDefinition = "text")
	private String codeCodeNumber;

	@Column(name = "_codeCodeNotation", columnDefinition = "text")
	private String codeCodeNotation;

	@Column(name = "_promulgationInfoPlace", columnDefinition = "text")
	private String promulgationInfoPlace;

	@Column(name = "_promulgationInfoPromulgationDate", columnDefinition = "text")
	private String promulgationInfoPromulgationDate;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_documentTypeId", referencedColumnName = "id")
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	private DocumentType documentType;

	@Column(name = "_subject", columnDefinition = "text")
	private String subject;

	@Column(name = "_content", columnDefinition = "text")
	private String content;

	@Column(name = "_pageAmount")
	private Integer pageAmount;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "_documentcatalog_attachments")
	private List<Attachment> attachments;

	@Column(name = "_steeringType")
	private Integer steeringType;

	@Column(name = "_businessDocReason", columnDefinition = "text")
	private String businessDocReason;

	@Column(name = "_businessDocumentId", columnDefinition = "text")
	private String businessDocumentId;

	@Column(name = "_senderDocId", columnDefinition = "text")
	private String senderDocId;

	@Column(name = "_receiverDocId", columnDefinition = "text")
	private String receiverDocId;

	@Column(name = "_enable", columnDefinition = "text")
	private Boolean enable;

	@Column(name = "_disableReason", columnDefinition = "text")
	private String disableReason;

	@Column(name = "_isPublic", columnDefinition = "text")
	private Boolean isPublic;

	@ManyToMany(cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	List<Organization> allowedOrganizations;

}
