package vn.ript.ssadapter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.EDoc;

public interface EDocRepository extends JpaRepository<EDoc, String> {
    @Query(value = "SELECT * FROM edoc WHERE _service_type=:serviceType AND _message_type=:messageType AND from_organization_id=:fromOrganization", nativeQuery = true)
    List<EDoc> findByServiceTypeAndMessageTypeAndFromOrganization(@Param("serviceType") String serviceType, @Param("messageType") String messageType, @Param("fromOrganization") String fromOrganization);
    
    @Query(value = "SELECT * FROM edoc WHERE _service_type=:serviceType AND _message_type=:messageType AND from_organization_id!=:fromOrganization", nativeQuery = true)
    List<EDoc> findByServiceTypeAndMessageTypeAndFromOrganizationNot(@Param("serviceType") String serviceType, @Param("messageType") String messageType, @Param("fromOrganization") String fromOrganization);
}
