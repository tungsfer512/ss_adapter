package vn.ript.ssadapter.controller.apilienthongvanban;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.vnpt.xml.status.Status;
import com.vnpt.xml.status.header.MessageStatus;

import vn.ript.ssadapter.model.EDoc;
import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.service.EDocService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXML;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/lienthong/v1/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EdocStatusLienThongController {

    @Autowired
    EDocService eDocService;
    @Autowired
    OrganizationService organizationService;

    // Gui file edxml giua cac SS, extract file, cac thong tin va luu vao DB cua ben
    // nhan
    @PostMapping(value = "/update")
    public ResponseEntity<Map<String, Object>> sendStatusEdoc(
            @RequestPart(name = "file", required = true) MultipartFile file,
            @RequestHeader(name = "from", required = true) String from,
            @RequestHeader(name = "docId", required = true) String docId) {
        try {
            String UUID = Utils.UUID();
            if (!file.isEmpty()) {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                String fileName = UUID + ".edxml";
                Path fileNameAndPath = Paths.get(Utils.EDocDir, fileName);
                Files.write(fileNameAndPath, file.getBytes());
                
                String edoc_64 = Utils.encodeEdXmlFileToBase64(fileNameAndPath.toString());

                Optional<Organization> checkFrom = organizationService.findByCode(from);
                Optional<Organization> checkTo = organizationService.findByCode(Utils.SS_ID);
                
                if (!checkFrom.isPresent() || !checkTo.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                }
                
                EDoc eDoc = new EDoc(UUID, UUID, UUID, docId, "eDoc", "status", Utils.datetime_now(), Utils.datetime_now(), edoc_64, checkFrom.get(), checkTo.get(), Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_GUI, Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_GUI, "Tieu de trang thai moi da nhan", "Notation trang thai moi da nhan", Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN.get(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_GUI), Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN.get(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_GUI), "Mo ta trang thai moi da nhan");

                Optional<EDoc> checkPeDoc = eDocService.findByDocId(docId);
                if (!checkPeDoc.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay van ban");
                }

                EDoc peDoc = checkPeDoc.get();
                Status edxmlStatus = EdXML.readStatus(fileName);
                System.out.println(edxmlStatus.toString());
                System.out.println("+================");
                Header header = edxmlStatus.getHeader();
                System.out.println(header.toString());
                System.out.println("+================");
                MessageStatus messageStatus = (MessageStatus) header.getMessageHeader();
                System.out.println(messageStatus.toString());
                System.out.println("+================");

                String statusCode = messageStatus.getStatusCode();
                String statusDescription = messageStatus.getDescription();
                peDoc.setUpdated_time(Utils.datetime_now());
                peDoc.setSendStatus(statusCode);
                peDoc.setReceiveStatus(statusCode);
                peDoc.setSendStatusDesc(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN.get(statusCode));
                peDoc.setReceiveStatusDesc(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN.get(statusCode));
                peDoc.setDescription(statusDescription);

                eDocService.saveEDoc(eDoc);
                eDocService.updateEDoc(peDoc);

                return CustomResponse.Response_no_data(200);
            } else {
                return CustomResponse.Response_data(500, "Loi Loi");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
