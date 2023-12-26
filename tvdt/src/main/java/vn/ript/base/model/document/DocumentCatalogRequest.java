package vn.ript.base.model.document;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "_documentcatalog_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class DocumentCatalogRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_fromId", referencedColumnName = "id")
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	private Organization organization;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "_documentCatalog", referencedColumnName = "id")
	@JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
	private DocumentCatalog documentCatalog;

	@Column(name = "_description", columnDefinition = "text")
	private String description;

	@Column(name = "_status", columnDefinition = "text")
	private String status;

	@Column(name = "_statusDesc", columnDefinition = "text")
	private String statusDesc;

	@Column(name = "_timestamp", columnDefinition = "text")
	private String timestamp;

}
