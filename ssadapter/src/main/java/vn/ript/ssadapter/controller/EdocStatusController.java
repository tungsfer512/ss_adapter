package vn.ript.ssadapter.controller;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vnpt.xml.base.Content;

import vn.ript.ssadapter.model.EDoc;
import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.service.EDocService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomHttpRequest;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXMLBuild;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/v1/edocs/status")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EdocStatusController {

    @Autowired
    EDocService eDocService;
    @Autowired
    OrganizationService organizationService;

    @PostMapping(value = "/update")
    public ResponseEntity<Map<String, Object>> sendStatusEdoc(
            @RequestHeader(name = "docId", required = true) String docId,
            @RequestHeader(name = "sendDocId", required = true) String sendDocId,
            @RequestHeader(name = "status", required = true) String status,
            @RequestHeader(name = "to", required = true) String to) {
        try {
            Content content;
            String id = Utils.UUID();
            String senderDocId = Utils.UUID();
            content = EdXMLBuild.create_status(status);
            Path edxmlFilePath = content.getContent().toPath();

            String edoc_64 = Utils.encodeEdXmlFileToBase64(edxmlFilePath.toAbsolutePath().toString());

            Optional<Organization> checkFrom = organizationService.findByCode(Utils.SS_ID);
            Optional<Organization> checkTo = organizationService.findByCode(to);

            if (!checkFrom.isPresent() || !checkTo.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            
            Optional<EDoc> checkEDoc = eDocService.findByDocId(docId);
            if(!checkEDoc.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay van ban");
            }
            EDoc eDoc = checkEDoc.get();

            EDoc eDocStatus = new EDoc(id, senderDocId, null, docId, "eDoc", "status", Utils.datetime_now(), Utils.datetime_now(),
                    edoc_64, checkFrom.get(), checkTo.get(), Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN,
                    Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN, "Tieu de trang thai moi da gui",
                    "Notation trang thai moi da gui",
                    Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN
                            .get(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN),
                    Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN
                            .get(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN),
                    "Mo ta trang thai moi da gui");

            String subsystem_code = to.replace(':', '/');
            String xRoadClient = Utils.SS_ID.replace(':', '/');
            String url = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/lienthongvanban/edocs/status/update";
            Map<String, String> headers = new HashMap<>();
            headers.put("from", Utils.SS_ID);
            headers.put("docId", sendDocId);
            headers.put("X-Road-Client", xRoadClient);
            CustomHttpRequest customHttpRequest = new CustomHttpRequest("POST", url, headers);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody("file", content.getContent());
            HttpEntity multipartHttpEntity = multipartEntityBuilder.build();

            HttpResponse httpResponse = customHttpRequest.request(multipartHttpEntity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                eDoc.setSendStatus(status);
                eDoc.setReceiveStatus(status);
                eDoc.setSendStatusDesc(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN.get(status));
                eDoc.setReceiveStatusDesc(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN.get(status));
                eDocService.saveEDoc(eDocStatus);
                eDocService.updateEDoc(eDoc);
                return CustomResponse.Response_no_data(200);
            } else {
                return CustomResponse.Response_data(500, httpResponse.getStatusLine());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    // Get danh sach VBDT, goi tin trang thai da nhan
    @GetMapping("/getReceivedStatusEdocList")
    public ResponseEntity<Map<String, Object>> getReceivedStatusEdocList() {
        try {
            Optional<Organization> checkOrganization = organizationService.findByCode(Utils.SS_ID);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<EDoc> edocs = eDocService.getReceivedStatusEdocList(Utils.SS_ID);
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    // Get danh sach VBDT, goi tin trang thai da gui
    @GetMapping("/getSentStatusEdocList")
    public ResponseEntity<Map<String, Object>> getSentStatusEdocList() {
        try {
            Optional<Organization> checkOrganization = organizationService.findByCode(Utils.SS_ID);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<EDoc> edocs = eDocService.getSentStatusEdocList(Utils.SS_ID);
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
