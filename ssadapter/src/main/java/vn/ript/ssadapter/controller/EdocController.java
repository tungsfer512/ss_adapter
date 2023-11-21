package vn.ript.ssadapter.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/api/v1/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EdocController {

    @Autowired
    EDocService eDocService;
    @Autowired
    OrganizationService organizationService;

    @PostMapping(value = "/newedoc")
    public ResponseEntity<Map<String, Object>> sendEdoc(
            @RequestPart(name = "file", required = false) MultipartFile file,
            @RequestHeader(name = "to", required = true) String to) {
        try {
            Content content;
            String UUID = Utils.UUID();
            if (!file.isEmpty()) {
                String originFileName = file.getOriginalFilename();
                List<String> attachFileInfo = Arrays.asList(originFileName.split("\\."));
                String attachFileExt = attachFileInfo.get(attachFileInfo.size() - 1);
                String attachFileName = UUID + "." + attachFileExt;
                Path attachFilePath = Paths.get(Utils.uploadDir, attachFileName);
                Files.write(attachFilePath, file.getBytes());
                TimeUnit.SECONDS.sleep(3);
                content = EdXMLBuild.createEdoc_new(attachFileName);
            } else {
                content = EdXMLBuild.createEdoc_new(null);
            }
            Path edxmlFilePath = content.getContent().toPath();

            String edoc_64 = Utils.encodeEdXmlFileToBase64(edxmlFilePath.toAbsolutePath().toString());

            Optional<Organization> checkFrom = organizationService.findByCode(Utils.SS_ID);
            Optional<Organization> checkTo = organizationService.findByCode(to);

            if (!checkFrom.isPresent() || !checkTo.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }

            EDoc eDoc = new EDoc(UUID, UUID, UUID, null, "eDoc", "edoc", Utils.datetime_now(), Utils.datetime_now(),
                    edoc_64, checkFrom.get(), checkTo.get(), Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN,
                    Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN, "Tieu de van ban moi da gui",
                    "Notation van ban moi da gui",
                    Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN
                            .get(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN),
                    Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.MO_TA_TRANG_THAI_VAN_BAN
                            .get(Constants.TRANG_THAI_VAN_BAN_LIEN_THONG.DA_DEN),
                    " Mo ta van ban moi da gui");

            String subsystem_code = to.replace(':', '/');
            String xRoadClient = Utils.SS_ID.replace(':', '/');
            String url = "https://" + Utils.SS_IP + "/r1/" + subsystem_code + "/lienthongvanban/edocs/newedoc";
            Map<String, String> headers = new HashMap<>();
            headers.put("from", Utils.SS_ID);
            headers.put("X-Road-Client", xRoadClient);
            CustomHttpRequest customHttpRequest = new CustomHttpRequest("POST", url,
                    headers);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody("file", content.getContent());
            HttpEntity multipartHttpEntity = multipartEntityBuilder.build();

            HttpResponse httpResponse = customHttpRequest.request(multipartHttpEntity);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                eDocService.saveEDoc(eDoc);
                return CustomResponse.Response_no_data(200);
            } else {
                return CustomResponse.Response_data(500, httpResponse.getStatusLine());
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    // Get danh sach VBDT, goi tin trang thai da nhan
    @GetMapping("/getReceivedEdocList")
    public ResponseEntity<Map<String, Object>> getReceivedEdocList() {
        try {
            Optional<Organization> checkOrganization = organizationService.findByCode(Utils.SS_ID);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<EDoc> edocs = eDocService.getReceivedEdocList(checkOrganization.get().getId());
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    // Get danh sach VBDT, goi tin trang thai da gui
    @GetMapping("/getSentEdocList")
    public ResponseEntity<Map<String, Object>> getSentEdocList() {
        try {
            Optional<Organization> checkOrganization = organizationService.findByCode(Utils.SS_ID);
            if (!checkOrganization.isPresent()) {
                return CustomResponse.Response_data(404, "Khong tim thay don vi");
            }
            List<EDoc> edocs = eDocService.getSentEdocList(checkOrganization.get().getId());
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

}
