package vn.ript.ssmanageadapter.model.initialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "_endpoint")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class Endpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "_ssId", columnDefinition = "text")
    private String ssId;

    @Column(name = "_name", columnDefinition = "text")
    private String name;

    @Column(name = "_method", columnDefinition = "text")
    private String method;

    @Column(name = "_path", columnDefinition = "text")
    private String path;

    @Column(name = "_description", columnDefinition = "text")
    private String description;

    @Column(name = "_inputDescription", columnDefinition = "text")
    private String inputDescription;

    @Column(name = "_outputDescription", columnDefinition = "text")
    private String outputDescription;

    @Column(name = "_isPublic", columnDefinition = "text")
    private Boolean isPublic;

    @Column(name = "_isForCitizen", columnDefinition = "text")
    private Boolean isForCitizen;

    @Column(name = "_type", columnDefinition = "text")
    private String type;

}
