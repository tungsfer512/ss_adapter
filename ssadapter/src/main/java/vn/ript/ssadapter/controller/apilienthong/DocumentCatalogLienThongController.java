package vn.ript.ssadapter.controller.apilienthong;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vnpt.xml.base.header.ResponseFor;
import com.vnpt.xml.ed.header.PromulgationInfo;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.model.document.Attachment;
import vn.ript.ssadapter.model.document.Document;
import vn.ript.ssadapter.model.document.DocumentType;
import vn.ript.ssadapter.model.document.ReplacementInfo;
import vn.ript.ssadapter.model.document.TraceHeader;
import vn.ript.ssadapter.model.document.UpdateReceiver;
import vn.ript.ssadapter.service.DocumentService;
import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.service.OrganizationService;
import vn.ript.ssadapter.utils.Constants;
import vn.ript.ssadapter.utils.CustomResponse;
import vn.ript.ssadapter.utils.EdXML;
import vn.ript.ssadapter.utils.Utils;

@RestController
@RequestMapping("/api/lienthong/v1/document-catalog/documents")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentCatalogLienThongController {
    
}
