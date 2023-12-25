package vn.ript.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.base.model.document.Document;
import vn.ript.base.repository.DocumentRepository;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    public List<Document> getReceivedEdocList(String toOrganizationId) {
        return documentRepository.findByServiceTypeAndMessageTypeAndToOrganization("eDoc","edoc", toOrganizationId);
    }
    
    public List<Document> getSentEdocList(String fromOrganizationId) {
        return documentRepository.findByServiceTypeAndMessageTypeAndFromOrganization("eDoc","edoc", fromOrganizationId);
    }
    
    public List<Document> getReceivedStatusEdocList(String toOrganizationId) {
        return documentRepository.findByServiceTypeAndMessageTypeAndToOrganization("eDoc","status", toOrganizationId);
    }
    
    public List<Document> getSentStatusEdocList(String fromOrganizationId) {
        return documentRepository.findByServiceTypeAndMessageTypeAndFromOrganization("eDoc","status", fromOrganizationId);
    }
    
    public Document sendEdoc(Document document) {
        return documentRepository.save(document);
    }

    public Optional<Document> findById(String id) {
        return documentRepository.findById(id);
    }

    public Optional<Document> findByDocumentId(String documentId) {
        return documentRepository.findByDocumentId(documentId);
    }

    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }
}