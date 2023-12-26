package vn.ript.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.base.model.document.DocumentType;
import vn.ript.base.repository.DocumentTypeRepository;


@Service
public class DocumentTypeService {

    @Autowired
    DocumentTypeRepository documentTypeRepository;

    public List<DocumentType> findAll() {
        return documentTypeRepository.findAll();
    }

    public Optional<DocumentType> findById(Integer id) {
        return documentTypeRepository.findById(id);
    }

    public Optional<DocumentType> findByTypeAndTypeName(Integer type, String typeName) {
        return documentTypeRepository.findByTypeAndTypeName(type, typeName);
    }

    public DocumentType saveDocumentType(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }
}
