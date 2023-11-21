package vn.ript.ssadapter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.model.EDoc;
import vn.ript.ssadapter.repository.EDocRepository;

@Service
public class EDocService {

    @Autowired
    EDocRepository eDocRepository;

    public List<EDoc> getReceivedEdocList(String toOrganizationId) {
        return eDocRepository.findByServiceTypeAndMessageTypeAndToOrganization("eDoc","edoc", toOrganizationId);
    }
    
    public List<EDoc> getSentEdocList(String fromOrganizationId) {
        return eDocRepository.findByServiceTypeAndMessageTypeAndFromOrganization("eDoc","edoc", fromOrganizationId);
    }
    
    public List<EDoc> getReceivedStatusEdocList(String toOrganizationId) {
        return eDocRepository.findByServiceTypeAndMessageTypeAndToOrganization("eDoc","status", toOrganizationId);
    }
    
    public List<EDoc> getSentStatusEdocList(String fromOrganizationId) {
        return eDocRepository.findByServiceTypeAndMessageTypeAndFromOrganization("eDoc","status", fromOrganizationId);
    }
    
    public EDoc sendEdoc(EDoc edoc) {
        return eDocRepository.save(edoc);
    }

    public Optional<EDoc> findByDocId(String docId) {
        return eDocRepository.findById(docId);
    }

    public EDoc updateEDoc(EDoc edoc) {
        return eDocRepository.save(edoc);
    }

    public EDoc saveEDoc(EDoc edoc) {
        return eDocRepository.save(edoc);
    }
}
