package vn.ript.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.ript.base.model.document.DocumentCatalogReport;

public interface DocumentCatalogReportRepository
        extends JpaRepository<DocumentCatalogReport, Integer>, JpaSpecificationExecutor<DocumentCatalogReport> {
}
