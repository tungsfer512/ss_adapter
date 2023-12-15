package vn.ript.ssadapter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.model.document.DocumentCatalogRequest;
import vn.ript.ssadapter.repository.DocumentCatalogRequestRepository;
import vn.ript.ssadapter.utils.CustomSpecification;

@Service
public class DocumentCatalogRequestService {

    @Autowired
    DocumentCatalogRequestRepository documentCatalogRRequestRepository;

    public List<DocumentCatalogRequest> findAll() {
        return documentCatalogRRequestRepository.findAll();
    }

    public List<DocumentCatalogRequest> findWithConditions(
            String status,
            String organizationId,
            String documentCatalogId,
            String sortTimestamp) {

        CustomSpecification<DocumentCatalogRequest> conditions = new CustomSpecification<>();

        List<Order> orders = new ArrayList<>();
        if (sortTimestamp != null) {
            if (sortTimestamp.equalsIgnoreCase("ASC")) {
                orders.add(new Order(Sort.Direction.ASC, "timestamp"));
            } else if (sortTimestamp.equalsIgnoreCase("DESC")) {
                orders.add(new Order(Sort.Direction.DESC, "timestamp"));
            }
        }
        Sort sort = Sort.by(orders);

        return documentCatalogRRequestRepository.findAll(conditions
                .and(conditions.condition("status", status, "equal"))
                .and(conditions.condition("organization.organId", organizationId, "equal"))
                .and(conditions.condition("documentCatalog.id", documentCatalogId, "equal")), sort);
    }

    public Optional<DocumentCatalogRequest> findById(Integer id) {
        return documentCatalogRRequestRepository.findById(id);
    }

    public DocumentCatalogRequest saveDocumentCatalogRequest(DocumentCatalogRequest document) {
        return documentCatalogRRequestRepository.save(document);
    }
}
