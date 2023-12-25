package vn.ript.base.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "_organization")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class Organization {

    @Id
    private String id;

    @Column(name = "_organId", columnDefinition = "text")
    private String organId;

    @Column(name = "_ssId", columnDefinition = "text")
    private String ssId;

    @Column(name = "_organizationInCharge", columnDefinition = "text")
    private String organizationInCharge;

    @Column(name = "_organName", columnDefinition = "text")
    private String organName;

    @Column(name = "_organAdd", columnDefinition = "text")
    private String organAdd;

    @Column(name = "_email", columnDefinition = "text")
    private String email;

    @Column(name = "_telephone", columnDefinition = "text")
    private String telephone;

    @Column(name = "_fax", columnDefinition = "text")
    private String fax;

    @Column(name = "_website", columnDefinition = "text")
    private String website;
}
