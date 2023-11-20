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

    public List<EDoc> getReceivedEdocList(String fromOrganization) {
        return eDocRepository.findByServiceTypeAndMessageTypeAndFromOrganizationNot("eDoc","edoc", fromOrganization);
    }
    
    public List<EDoc> getSentEdocList(String fromOrganization) {
        return eDocRepository.findByServiceTypeAndMessageTypeAndFromOrganization("eDoc","status", fromOrganization);
    }
    
    public List<EDoc> getReceivedStatusEdocList(String fromOrganization) {
        return eDocRepository.findByServiceTypeAndMessageTypeAndFromOrganizationNot("eDoc","status", fromOrganization);
    }
    
    public List<EDoc> getSentStatusEdocList(String fromOrganization) {
        return eDocRepository.findByServiceTypeAndMessageTypeAndFromOrganization("eDoc","edoc", fromOrganization);
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
