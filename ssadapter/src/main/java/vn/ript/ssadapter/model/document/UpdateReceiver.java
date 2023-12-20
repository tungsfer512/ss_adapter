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

    @Column(name = "_updateReceiverReceiverType")
    private Integer updateReceiverReceiverType;

    @Column(name = "_updateReceiverOrganId", columnDefinition = "text")
    private String updateReceiverOrganId;

}
