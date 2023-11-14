package vn.ript.ssadapter.controller.apilienthongvanban;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vnpt.xml.base.Content;
import com.vnpt.xml.base.attachment.Attachment;
import com.vnpt.xml.base.header.Header;
import com.vnpt.xml.base.header.Organization;
import com.vnpt.xml.ed.Ed;
import com.vnpt.xml.ed.header.MessageHeader;

import vn.ript.ssadapter.model.EDoc;
import vn.ript.ssadapter.service.EDocService;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXML;
import vn.ript.ssadapter.utils.EdXMLBuild;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/lienthong/v1/edocs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EdocLienThongController {

    @Autowired
    EDocService eDocService;

    // Gui file edxml giua cac SS, extract file, cac thong tin va luu vao DB cua ben
    // nhan
    @PostMapping(value = "/sendEdoc")
    public ResponseEntity<Map<String, Object>> sendEdoc(
            @RequestPart(name = "file", required = true) MultipartFile file,
            @RequestHeader(name = "from", required = true) String from,
            @RequestHeader(name = "servicetype", required = true) String serviceType,
            @RequestHeader(name = "messagetype", required = true) String messageType) {
        try {
            if (!file.isEmpty()) {
                String originFileName = file.getOriginalFilename();
                Path fileNameAndPath = Paths.get(Utils.EDocDir, originFileName);
                Files.write(fileNameAndPath, file.getBytes());

                Ed ed = EdXML.readEdoc(originFileName);
                Header header = ed.getHeader();
                MessageHeader messageHeader = (MessageHeader) header.getMessageHeader();
                Organization organization = (Organization) messageHeader.getFrom();
                System.out.println("organization: " + organization.toString());
                String organizationId = organization.getOrganId();
                System.out.println("organizationId: " + organizationId);

                String organizationInCharge = organization.getOrganizationInCharge();
                System.out.println("organizationInCharge: " + organizationInCharge);

                String organizationName = organization.getOrganName();
                System.out.println("organizationName: " + organizationName);

                String organizationAddress = organization.getOrganAdd();
                System.out.println("organizationAddress: " + organizationAddress);

                String organizationEmail = organization.getEmail();
                System.out.println("organizationEmail: " + organizationEmail);

                String organizationPhone = organization.getTelephone();
                System.out.println("organizationPhone: " + organizationPhone);

                String organizationFax = organization.getFax();
                System.out.println("organizationFax: " + organizationFax);

                String organizationWeb = organization.getWebsite();
                System.out.println("organizationWeb: " + organizationWeb);

                List<Attachment> attachments = ed.getAttachments();
                System.out.println("attachments: " + attachments.toString());

                File fileInEdxml = attachments.get(0).getContent();
                String contentType = attachments.get(0).getContentType();
                System.out.println("contentType: " + contentType);
                String mimeType = attachments.get(0).getMimeType();
                System.out.println("mimeType: " + mimeType);
                String fileName = attachments.get(0).getName();
                System.out.println("fileName: " + fileName);

                Path fileNameAndPath2 = Paths.get(Utils.uploadDir, fileName);
                Files.write(fileNameAndPath2, Files.readAllBytes(fileInEdxml.toPath()));

                String edoc_64 = Utils.encodeEdXmlFileToBase64(fileNameAndPath.toString());

                EDoc eDoc = new EDoc(originFileName, originFileName, originFileName, originFileName, serviceType,
                        messageType, Utils.datetime_now(), Utils.datetime_now(), edoc_64, "from", "to", "wait",
                        "wait", originFileName, "notation", "send status desc", "receive status desc");

                eDocService.saveEDoc(eDoc);

                return CustomResponse.Response_no_data(200);
            } else {
                return CustomResponse.Response_data(500, "Loi loi");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping(value = "/test")
    public ResponseEntity<Map<String, Object>> test(
            @RequestPart(name = "certificate", required = true) MultipartFile certificate,
            @RequestPart(name = "certificate_profile_info", required = true) String certificate_profile_info) {
        try {
            if (!certificate.isEmpty()) {
                String originFileName = certificate.getOriginalFilename();
                return CustomResponse.Response_data(200, originFileName + " " + certificate_profile_info);
            } else {
                return CustomResponse.Response_data(500, "Loi loi");
            }
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }
}
