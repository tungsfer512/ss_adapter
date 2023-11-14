package vn.ript.ssadapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.EDoc;

public interface EDocRepository extends JpaRepository<EDoc, String> {
    List<EDoc> findByServiceTypeAndMessageType(@Param("serviceType") String serviceType, @Param("messageType") String messageType);
    List<EDoc> findByFrom(@Param("from") String from);
    List<EDoc> findByFromNot(@Param("from") String from);
}
