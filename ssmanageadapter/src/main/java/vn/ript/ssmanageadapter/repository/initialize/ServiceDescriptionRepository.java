package vn.ript.ssmanageadapter.repository.initialize;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.ript.ssmanageadapter.model.initialize.ServiceDescription;

public interface ServiceDescriptionRepository extends JpaRepository<ServiceDescription, Integer> {
    Optional<ServiceDescription> findBySsId(@Param("ssId") String ssIs);
    List<ServiceDescription> findByOrganizationOrganId(@Param("organId") String organId);
}
