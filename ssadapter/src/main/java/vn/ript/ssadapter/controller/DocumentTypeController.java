package vn.ript.ssadapter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ript.ssadapter.model.document.DocumentType;
import vn.ript.ssadapter.service.DocumentTypeService;
import vn.ript.ssadapter.utils.CustomResponse;

@RestController
@RequestMapping("/api/v1/document/types")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DocumentTypeController {
    
    @Autowired
    DocumentTypeService documentTypeService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getDocumentTypeList() {
        try {
            List<DocumentType> documentTypes = documentTypeService.findAll();
            return CustomResponse.Response_data(200, documentTypes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<Map<String, Object>> addDocumentType(
            @RequestBody Map<String, Object> entity) {
        try {
            DocumentType documentType = new DocumentType();
            documentType.setType(Integer.parseInt(entity.get("type").toString()));
            documentType.setTypeDetail(entity.get("typeDetail").toString());
            documentType.setTypeName(entity.get("typeName").toString());
            DocumentType documentTypeRes = documentTypeService.saveDocumentType(documentType);
            return CustomResponse.Response_data(200, documentTypeRes);
        } catch (Exception e) {
            return CustomResponse.Response_data(500, e);
        }
    }
}
