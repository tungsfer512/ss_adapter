package vn.ript.ssadapter.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
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
import vn.ript.ssadapter.service.EDocService;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXMLBuild;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/v1/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EdocController {

    @Autowired
    EDocService eDocService;

    // Nhan va dong goi thong tin, file tu giao dien adapter ==> edoc va luu vao DB cua ben gui
    @PostMapping(value = "/sendEdoc") 
    public ResponseEntity<Map<String, Object>> sendEdoc(
            @RequestPart(name = "file", required = true) MultipartFile file,
            @RequestHeader(name = "from", required = true) String from,
            @RequestHeader(name = "servicetype", required = true) String serviceType,
            @RequestHeader(name = "messagetype", required = true) String messageType) {
        try {
            if (!file.isEmpty()) {
                String fileUUID = Utils.UUID();
                System.out.println(fileUUID);
                String originFileName = file.getOriginalFilename();
                System.out.println(originFileName);
                List<String> attachFileInfo = Arrays.asList(originFileName.split("\\."));
                System.out.println(attachFileInfo.get(0) + " ----" + attachFileInfo.get(1));
                String attachFileExt = attachFileInfo.get(attachFileInfo.size() - 1);
                String attachFileName = fileUUID + "." + attachFileExt;
                String edocFileName = fileUUID + ".edxml";
                System.out.println(attachFileName + "------" +  edocFileName);
                Path attachFilePath = Paths.get(Utils.uploadDir, attachFileName);
                System.out.println(attachFilePath);
                Files.write(attachFilePath, file.getBytes());
                System.out.println(attachFilePath);
                TimeUnit.SECONDS.sleep(3);
                Content content = EdXMLBuild.createEdoc_new(attachFileName);
                Path edxmlFilePath = content.getContent().toPath();
                System.out.println(attachFilePath);
                
                // TimeUnit.SECONDS.sleep(3);
                String edoc_64 = Utils.encodeEdXmlFileToBase64(edxmlFilePath.toAbsolutePath().toString());
                
                EDoc eDoc = new EDoc(fileUUID, fileUUID, fileUUID, fileUUID, serviceType, messageType, Utils.datetime_now(), Utils.datetime_now(), edoc_64, from, "to", "wait", "wait", originFileName, "notation", "send status desc", "receive status desc");
                
                // Call api Xroad lienthongvanban

                System.out.println(attachFilePath);
                eDocService.saveEDoc(eDoc);
                System.out.println(attachFilePath);

                return CustomResponse.Response_no_data(200);
            } else {
                return CustomResponse.Response_data(500, "Loi loi");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }
    
    @GetMapping("/getReceivedEdocList")
    public ResponseEntity<Map<String, Object>> getReceivedEdocList(
            @RequestHeader(name = "servicetype", required = true) String serviceType,
            @RequestHeader(name = "messagetype", required = true) String messageType) {
        try {
            List<EDoc> edocs = eDocService.getReceivedEdocList();
            // List<Object> res = new ArrayList<Object>();
            // for (EDoc edoc : edocs) {
            //     Object te = edoc;
            //     te.getClass().getDeclaredField("data").set(te, null);
            //     res.add(edoc);
            // }
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
             return CustomResponse.Response_data(500, e);
        }
    }

    @GetMapping("/getSentEdocList")
    public ResponseEntity<Map<String, Object>> getSentEdocList(
            @RequestHeader(name = "servicetype", required = true) String serviceType,
            @RequestHeader(name = "messagetype", required = true) String messageType) {
        try {
            List<EDoc> edocs = eDocService.getSentEdocList();
            return CustomResponse.Response_data(200, edocs);
        } catch (Exception e) {
             return CustomResponse.Response_data(500, e);
        }
    }
}
