package vn.ript.base.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.ript.base.model.document.Document;

public interface DocumentRepository extends JpaRepository<Document, String> {

    @Query(value = "SELECT * FROM _document WHERE _service_type=:serviceType AND _message_type=:messageType AND _from_id=:fromOrganizationId", nativeQuery = true)
    List<Document> findByServiceTypeAndMessageTypeAndFromOrganization(@Param("serviceType") String serviceType, @Param("messageType") String messageType, @Param("fromOrganizationId") String fromOrganizationId);
    
    @Query(value = "SELECT * FROM _document WHERE _service_type=:serviceType AND _message_type=:messageType AND _to_id=:toOrganizationId", nativeQuery = true)
    List<Document> findByServiceTypeAndMessageTypeAndToOrganization(@Param("serviceType") String serviceType, @Param("messageType") String messageType, @Param("toOrganizationId") String toOrganizationId);

    Optional<Document> findByDocumentId(String documentId);
}
