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

public class UpdateReceiver {

    @Column(name = "_updateReceiver_ReceiverType")
    private Integer updateReceiver_ReceiverType;

    @Column(name = "_updateReceiver_OrganId", columnDefinition = "text")
    private String updateReceiver_OrganId;

}
