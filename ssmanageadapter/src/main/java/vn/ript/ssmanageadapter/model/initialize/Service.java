package vn.ript.ssmanageadapter.model.initialize;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

@Entity
@Table(name = "_service")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "_ssId", columnDefinition = "text")
    private String ssId;

    @Column(name = "_description", columnDefinition = "text")
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = { "applications", "hibernateLazyInitializer" })
    private List<Endpoint> endpoints;

    @Column(name = "_isPublic", columnDefinition = "text")
    private Boolean isPublic;

    @Column(name = "_isForCitizen", columnDefinition = "text")
    private Boolean isForCitizen;

    @Column(name = "_type", columnDefinition = "text")
    private String type;

}
