package vn.ript.ssadapter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.model.document.DocumentCatalog;
import vn.ript.ssadapter.repository.DocumentCatalogRepository;

@Service
public class DocumentCatalogService {

    @Autowired
    DocumentCatalogRepository documentRepository;

    public List<DocumentCatalog> findAll() {
        return documentRepository.findAll();
    }

    public Optional<DocumentCatalog> findById(String id) {
        return documentRepository.findById(id);
    }

    public Optional<DocumentCatalog> findByDocumentId(String documentId) {
        return documentRepository.findByDocumentId(documentId);
    }

    public DocumentCatalog updateDocumentCatalog(DocumentCatalog document) {
        return documentRepository.save(document);
    }

    public DocumentCatalog saveDocumentCatalog(DocumentCatalog document) {
        return documentRepository.save(document);
    }
}
