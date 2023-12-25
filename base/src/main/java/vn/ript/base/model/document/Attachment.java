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
public class Attachment {

    @Column(name = "_attachmentContentType", columnDefinition = "text")
    private String attachmentContentType;

    @Column(name = "_attachmentContentId", columnDefinition = "text")
    private String attachmentContentId;

    @Column(name = "_attachmentDescription", columnDefinition = "text")
    private String attachmentDescription;

    @Column(name = "_attachmentContentTransferEncoded", columnDefinition = "text")
    private String attachmentContentTransferEncoded;

    @Column(name = "_attachmentAttachmentName", columnDefinition = "text")
    private String attachmentAttachmentName;
}
