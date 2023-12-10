package vn.ript.ssadapter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.ript.ssadapter.model.document.DocumentCatalog;

public interface DocumentCatalogRepository extends JpaRepository<DocumentCatalog, String> {

    Optional<DocumentCatalog> findByDocumentId(String documentId);
}
