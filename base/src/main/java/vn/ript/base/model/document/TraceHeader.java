package vn.ript.base.model.document;

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

    @Column(name = "_traceHeaderOrganId", columnDefinition = "text")
    String traceHeaderOrganId;

    @Column(name = "_traceHeaderTimestamp", columnDefinition = "text")
    String traceHeaderTimestamp;

}
