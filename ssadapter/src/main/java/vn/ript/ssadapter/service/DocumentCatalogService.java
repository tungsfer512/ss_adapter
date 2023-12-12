package vn.ript.ssadapter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.model.document.DocumentCatalog;
import vn.ript.ssadapter.repository.DocumentCatalogRepository;
import vn.ript.ssadapter.utils.CustomSpecification;

@Service
public class DocumentCatalogService {

    @Autowired
    DocumentCatalogRepository documentCatalogRepository;

    public List<DocumentCatalog> findAll() {
        return documentCatalogRepository.findAll();
    }

    public List<DocumentCatalog> findWithConditions(
            String fromId,
            String codeNumber,
            String codeNotation,
            String promulgationInfoPlace,
            String[] promulgationInfoPromulgationDates,
            Integer documentTypeId,
            String subject,
            Integer steeringType,
            String businessDocumentId,
            Boolean enable,
            Boolean isPublic,
            String sortDate,
            String sortSubject) {

        CustomSpecification<DocumentCatalog> conditions = new CustomSpecification<>();

        String enable_str = null;
        String isPublic_str = null;
        if (enable != null) {
            enable_str = enable.toString();
        }
        if (isPublic != null) {
            isPublic_str = isPublic.toString();
        }
        String promulgationInfoPromulgationDates_str = null;
        if (promulgationInfoPromulgationDates != null && promulgationInfoPromulgationDates.length == 2) {
            promulgationInfoPromulgationDates_str = promulgationInfoPromulgationDates[0] + "-"
                    + promulgationInfoPromulgationDates[1];
        }

        List<Order> orders = new ArrayList<>();
        if (sortDate != null) {
            if (sortDate.equalsIgnoreCase("ASC")) {
                orders.add(new Order(Sort.Direction.ASC, "promulgationInfoPromulgationDate"));
            } else if (sortDate.equalsIgnoreCase("DESC")) {
                orders.add(new Order(Sort.Direction.DESC, "promulgationInfoPromulgationDate"));
            }
        }
        if (sortSubject != null) {
            if (sortSubject.equalsIgnoreCase("ASC")) {
                orders.add(new Order(Sort.Direction.ASC, "subject"));
            } else if (sortSubject.equalsIgnoreCase("DESC")) {
                orders.add(new Order(Sort.Direction.DESC, "subject"));
            }
        }
        Sort sort = Sort.by(orders);

        return documentCatalogRepository.findAll(conditions
                .and(conditions.condition("from.organId", fromId, "equal"))
                .and(conditions.condition("codeCodeNotation", codeNotation, "like"))
                .and(conditions.condition("codeCodeNumber", codeNumber, "like"))
                .and(conditions.condition("promulgationInfoPlace", promulgationInfoPlace, "like"))
                .and(conditions.condition("promulgationInfoPromulgationDate", promulgationInfoPromulgationDates_str,
                        "date_between"))
                .and(conditions.condition("documentType.id", documentTypeId, "equal"))
                .and(conditions.condition("subject", subject, "like"))
                .and(conditions.condition("steeringType", steeringType, "equal"))
                .and(conditions.condition("businessDocumentId", businessDocumentId, "equal"))
                .and(conditions.condition("enable", enable_str, "equal"))
                .and(conditions.condition("isPublic", isPublic_str, "equal")), sort);
    }

    public Optional<DocumentCatalog> findById(String id) {
        return documentCatalogRepository.findById(id);
    }

    public DocumentCatalog saveDocumentCatalog(DocumentCatalog document) {
        return documentCatalogRepository.save(document);
    }
}
