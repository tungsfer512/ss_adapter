package vn.ript.ssadapter.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Entity
@Table(name = "edoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class EDoc {
    
    @Id
    String id;
    @Column(name = "_senderDocId", columnDefinition = "text", updatable = false)
    String senderDocId;
    @Column(name = "_receiverDocId", columnDefinition = "text", updatable = false)
    String receiverDocId;
    @Column(name = "_pid", columnDefinition = "text", updatable = false)
    String pid;
    @Column(name = "_serviceType", columnDefinition = "text", updatable = false)
    String serviceType;
    @Column(name = "_messageType", columnDefinition = "text", updatable = false)
    String messageType;
    @Column(name = "_created_time", columnDefinition = "text", updatable = false)
    String created_time;
    @Column(name = "_updated_time", columnDefinition = "text", updatable = true)
    String updated_time;
    @Column(name = "_data", columnDefinition = "text", updatable = false)
    String data;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(
		name = "from_organization_id",
		referencedColumnName = "id",
        columnDefinition = "text",
		updatable = false,
		nullable = false
	)
	@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer" })
	Organization fromOrganization;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(
		name = "to_organization_id",
		referencedColumnName = "id",
        columnDefinition = "text",
		updatable = false,
		nullable = false
	)
	@JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer" })
	Organization toOrganization;
    @Column(name = "_sendStatus", columnDefinition = "text", updatable = true)
    String sendStatus;
    @Column(name = "_receiveStatus", columnDefinition = "text", updatable = true)
    String receiveStatus;
    @Column(name = "_title", columnDefinition = "text", updatable = false)
    String title;
    @Column(name = "_notation", columnDefinition = "text", updatable = false)
    String notation;
    @Column(name = "_sendStatusDesc", columnDefinition = "text", updatable = true)
    String sendStatusDesc;
    @Column(name = "_receiveStatusDesc", columnDefinition = "text", updatable = true)
    String receiveStatusDesc;
    @Column(name = "_description", columnDefinition = "text", updatable = true)
    String description;

}
