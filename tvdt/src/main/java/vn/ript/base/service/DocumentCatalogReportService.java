package vn.ript.base.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import vn.ript.base.model.document.DocumentCatalogReport;
import vn.ript.base.repository.DocumentCatalogReportRepository;
import vn.ript.base.utils.CustomSpecification;

@Service
public class DocumentCatalogReportService {

    @Autowired
    DocumentCatalogReportRepository documentCatalogReportRepository;

    public List<DocumentCatalogReport> findAll() {
        return documentCatalogReportRepository.findAll();
    }

    public List<DocumentCatalogReport> findWithConditions(
            String status,
            String organizationId,
            String documentCatalogId,
            String sortTimestamp) {

        CustomSpecification<DocumentCatalogReport> conditions = new CustomSpecification<>();

        List<Order> orders = new ArrayList<>();
        if (sortTimestamp != null) {
            if (sortTimestamp.equalsIgnoreCase("ASC")) {
                orders.add(new Order(Sort.Direction.ASC, "timestamp"));
            } else if (sortTimestamp.equalsIgnoreCase("DESC")) {
                orders.add(new Order(Sort.Direction.DESC, "timestamp"));
            }
        }
        Sort sort = Sort.by(orders);

        return documentCatalogReportRepository.findAll(conditions
                .and(conditions.condition("status", status, "equal"))
                .and(conditions.condition("organization.organId", organizationId, "equal"))
                .and(conditions.condition("documentCatalog.id", documentCatalogId, "equal")), sort);
    }

    public Optional<DocumentCatalogReport> findById(Integer id) {
        return documentCatalogReportRepository.findById(id);
    }

    public DocumentCatalogReport saveDocumentCatalogReport(DocumentCatalogReport document) {
        return documentCatalogReportRepository.save(document);
    }
}
