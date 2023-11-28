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
public class Attachment {

    @Column(name = "_attachment_ContentType", columnDefinition = "text")
    private String attachment_ContentType;

    @Column(name = "_attachment_ContentId", columnDefinition = "text")
    private String attachment_ContentId;

    @Column(name = "_attachment_Description", columnDefinition = "text")
    private String attachment_Description;

    @Column(name = "_attachment_ContentTransferEncoded", columnDefinition = "text")
    private String attachment_ContentTransferEncoded;

    @Column(name = "_attachment_AttachmentName", columnDefinition = "text")
    private String attachment_AttachmentName;
}
