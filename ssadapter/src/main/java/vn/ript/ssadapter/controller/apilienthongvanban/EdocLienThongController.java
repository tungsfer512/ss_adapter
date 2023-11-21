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

import vn.ript.ssadapter.model.EDoc;
import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.service.EDocService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/lienthong/v1/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EdocLienThongController {

    @Autowired
    EDocService eDocService;
    @Autowired
    OrganizationService organizationService;

    // Gui file edxml giua cac SS, extract file, cac thong tin va luu vao DB cua ben
    // nhan
    @PostMapping(value = "/newedoc")
    public ResponseEntity<Map<String, Object>> sendEdoc(
            @RequestPart(name = "file", required = true) MultipartFile file,
            @RequestHeader(name = "from", required = true) String from) {
        try {
            String id = Utils.UUID();
            String receiverDocId = Utils.UUID();
            if (!file.isEmpty()) {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                String originFileName = file.getOriginalFilename();
                Path fileNameAndPath = Paths.get(Utils.EDocDir, originFileName);
                Files.write(fileNameAndPath, file.getBytes());
                
                String edoc_64 = Utils.encodeEdXmlFileToBase64(fileNameAndPath.toString());

                Optional<Organization> checkFrom = organizationService.findByCode(from);
                Optional<Organization> checkTo = organizationService.findByCode(Utils.SS_ID);
                
                if (!checkFrom.isPresent() || !checkTo.isPresent()) {
                    return CustomResponse.Response_data(404, "Khong tim thay don vi");
                }
                
                EDoc eDoc = new EDoc(id, null, receiverDocId, null, "eDoc", "edoc", Utils.datetime_now(), Utils.datetime_now(), edoc_64, checkFrom.get(), checkTo.get(), Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN, Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN, "Tieu de van ban moi da nhan", "Notation van ban moi da nhan", Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN.get(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN), Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN.get(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN),  "Mo ta van ban moi da nhan");

                eDocService.saveEDoc(eDoc);

                return CustomResponse.Response_no_data(200);
            } else {
                return CustomResponse.Response_data(500, "Loi Loi");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
