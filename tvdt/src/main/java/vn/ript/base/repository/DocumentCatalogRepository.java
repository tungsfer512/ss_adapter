package vn.ript.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.ript.base.model.document.DocumentCatalog;

public interface DocumentCatalogRepository
        extends JpaRepository<DocumentCatalog, String>, JpaSpecificationExecutor<DocumentCatalog> {
}
