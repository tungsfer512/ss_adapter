package vn.ript.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.ript.base.model.document.DocumentCatalogRequest;

public interface DocumentCatalogRequestRepository
        extends JpaRepository<DocumentCatalogRequest, Integer>, JpaSpecificationExecutor<DocumentCatalogRequest> {
}
