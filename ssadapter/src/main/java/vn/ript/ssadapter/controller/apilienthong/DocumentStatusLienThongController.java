package vn.ript.ssadapter.controller.apilienthong;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vnpt.xml.base.header.Header;
import com.vnpt.xml.base.header.ResponseFor;
import com.vnpt.xml.base.header.StaffInfo;
import com.vnpt.xml.status.Status;
import com.vnpt.xml.status.header.MessageStatus;

import vn.ript.ssadapter.model.document.Document;
import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.service.DocumentService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXML;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/lienthong/v1/document/status")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentStatusLienThongController {

    @Autowired
    DocumentService documentService;
    @Autowired
    OrganizationService organizationService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, Object>> sendStatusEdoc(
            @RequestPart(name = "file", required = true) MultipartFile file,
            @RequestHeader(name = "docId", required = true) String docId) {
        try {
            if (!file.isEmpty()) {

                String document_64 = Utils.ENCODE_TO_BASE64(file.getBytes());

                Status statusEdxml = EdXML.readStatus(file.getInputStream());
                Header header = statusEdxml.getHeader();
                MessageStatus messageStatus = (MessageStatus) header.getMessageHeader();
                ResponseFor responseFor = messageStatus.getResponseFor();
                StaffInfo staffInfo = messageStatus.getStaffInfo();
                String from_organ_id = messageStatus.getFrom().getOrganId();

                Optional<Organization> checkFrom = organizationService.findByOrganId(from_organ_id);
                Optional<Organization> checkTo = organizationService.findByOrganId(Utils.SS_ID);

                if (!checkFrom.isPresent() || !checkTo.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                }

                String status = Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.maTrangThai();
                String statusDesc = Constants.TRANG_THAI_LIEN_THONG.THANH_CONG.moTaTrangThai();

                String status_status_code = messageStatus.getStatusCode();
                String status_tmp = Constants.TRANG_THAI_GOI_TIN.DA_GUI_KHONG_PHAN_HOI.maTrangThai();
                if (status_status_code.equalsIgnoreCase(Constants.TRANG_THAI_VAN_BAN.DA_YEU_CAU_CAP_NHAT.maTrangThai())
                        || status_status_code
                                .equalsIgnoreCase(Constants.TRANG_THAI_VAN_BAN.DA_YEU_CAU_CAP_NHAT.maTrangThai())) {
                    status_tmp = Constants.TRANG_THAI_GOI_TIN.CHO_PHAN_HOI.maTrangThai();
                }

                Document document = new Document(
                        Utils.UUID(),
                        checkFrom.get(),
                        checkTo.get(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        responseFor.getOrganId(),
                        responseFor.getCode(),
                        Utils.DATE_TO_YYYY_MM_DD(responseFor.getPromulgationDate()),
                        responseFor.getDocumentId(),
                        null,
                        null,
                        messageStatus.getStatusCode(),
                        messageStatus.getDescription(),
                        Utils.DATETIME_NOW(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        staffInfo.getDepartment(),
                        staffInfo.getStaff(),
                        staffInfo.getMobile(),
                        staffInfo.getEmail(),
                        null,
                        null,
                        null,
                        "eDoc",
                        "status",
                        null,
                        null,
                        status,
                        statusDesc,
                        Constants.TRANG_THAI_GOI_TIN.getByMaTrangThai(status_tmp).maTrangThai(),
                        Constants.TRANG_THAI_GOI_TIN.getByMaTrangThai(status_tmp).maTrangThai(),
                        Constants.TRANG_THAI_GOI_TIN.getByMaTrangThai(status_tmp).moTaTrangThaiGui(),
                        Constants.TRANG_THAI_GOI_TIN.getByMaTrangThai(status_tmp).moTaTrangThaiNhan(),
                        document_64);

                Optional<Document> checkPdocument = documentService.findByDocumentId(docId);
                if (!checkPdocument.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay van ban");
                }

                Document pdocument = checkPdocument.get();

                pdocument.setSendStatus(status_status_code);
                pdocument.setReceiveStatus(status_status_code);
                pdocument.setSendStatusDesc(
                        Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).moTaTrangThaiGui()
                        + "\n (" + messageStatus.getDescription() + ")");
                pdocument.setReceiveStatusDesc(
                        Constants.TRANG_THAI_VAN_BAN.getByMaTrangThai(status_status_code).moTaTrangThaiNhan()
                        + "\n (" + messageStatus.getDescription() + ")");

                documentService.saveDocument(document);
                documentService.updateDocument(pdocument);

                return CustomResponse.Response_no_data(201);
            } else {
                return CustomResponse.Response_data(500, "Loi Loi");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
