package vn.ript.ssadapter.model;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class Organization {
    
    @Id
    String id;
    @Column(name = "_code", columnDefinition = "text", updatable = true)
    String code;
    @Column(name = "_pid", columnDefinition = "text", updatable = true)
    String pid;
    @Column(name = "_name", columnDefinition = "text", updatable = true)
    String name;
    @Column(name = "_address", columnDefinition = "text", updatable = true)
    String address;
    @Column(name = "_email", columnDefinition = "text", updatable = true)
    String email;
    @Column(name = "_telephone", columnDefinition = "text", updatable = true)
    String telephone;
    @Column(name = "_fax", columnDefinition = "text", updatable = true)
    String fax;
    @Column(name = "_website", columnDefinition = "text", updatable = true)
    String website;
    
}
