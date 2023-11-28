package vn.ript.ssadapter.model.document;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class TraceHeader {

    @Column(name = "_traceHeader_OrganId", columnDefinition = "text")
    String traceHeader_OrganId;

    @Column(name = "_traceHeader_Timestamp", columnDefinition = "text")
    String traceHeader_Timestamp;

}