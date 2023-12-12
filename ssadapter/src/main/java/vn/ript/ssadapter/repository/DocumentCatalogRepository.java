package vn.ript.ssadapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.ript.ssadapter.model.document.DocumentCatalog;

public interface DocumentCatalogRepository
        extends JpaRepository<DocumentCatalog, String>, JpaSpecificationExecutor<DocumentCatalog> {
}
