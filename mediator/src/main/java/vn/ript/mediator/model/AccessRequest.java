package vn.ript.mediator.model;

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
import vn.ript.mediator.model.initialize.Endpoint;
import vn.ript.mediator.model.initialize.Service;

@Entity
@Table(name = "_access_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class AccessRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "_externalId", columnDefinition = "text")
    private String externalId;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "_fromId", referencedColumnName = "id")
    @JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
    private Organization from;

    @Column(name = "_fromName", columnDefinition = "text")
    private String fromName;
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "_toId", referencedColumnName = "id")
    @JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
    private Organization to;

    @Column(name = "_type", columnDefinition = "text")
    private String type;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "_serviceId", referencedColumnName = "id")
    @JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
    private Service service;
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "_endpointId", referencedColumnName = "id")
    @JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
    private Endpoint endpoint;

    @Column(name = "_externalServiceId")
    private Integer externalServiceId;
    
    @Column(name = "_externalEndpointId")
    private Integer externalEndpointId;

    @Column(name = "_description", columnDefinition = "text")
    private String description;

    @Column(name = "_status", columnDefinition = "text")
    private String status;
    
    @Column(name = "_createAt", columnDefinition = "text")
    private String createAt;
    
    @Column(name = "_updateAt", columnDefinition = "text")
    private String updateAt;

    @Column(name = "_declinedReason", columnDefinition = "text")
    private String declinedReason;
}
