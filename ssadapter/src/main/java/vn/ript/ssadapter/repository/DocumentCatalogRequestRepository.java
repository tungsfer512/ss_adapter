package vn.ript.ssadapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.ript.ssadapter.model.document.DocumentCatalogRequest;

public interface DocumentCatalogRequestRepository
        extends JpaRepository<DocumentCatalogRequest, Integer>, JpaSpecificationExecutor<DocumentCatalogRequest> {
}
