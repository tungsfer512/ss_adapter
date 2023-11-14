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

    public List<EDoc> getReceivedEdocList() {
        return eDocRepository.findByFromNot("me");
    }
    public List<EDoc> getSentEdocList() {
        return eDocRepository.findByFrom("me");
    }
    
    public EDoc sendEdoc(EDoc edoc) {
        return eDocRepository.save(edoc);
    }

    public Optional<EDoc> findByDocId(String docId) {
        return eDocRepository.findById(docId);
    }

    public EDoc updateStatusByDocId(EDoc edoc) {
        return eDocRepository.save(edoc);
    }

    public EDoc saveEDoc(EDoc edoc) {
        return eDocRepository.save(edoc);
    }
}
