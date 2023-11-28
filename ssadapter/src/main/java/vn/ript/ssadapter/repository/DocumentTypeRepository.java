package vn.ript.ssadapter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.document.DocumentType;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {

    Optional<DocumentType> findByTypeAndTypeName(@Param("type") Integer type, @Param("typeName") String typeName);
}
