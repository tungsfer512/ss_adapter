package vn.ript.ssadapter.repository.initiallize;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.initialize.ServiceDescription;

public interface ServiceDescriptionRepository extends JpaRepository<ServiceDescription, Integer> {
    Optional<ServiceDescription> findBySsId(@Param("ssId") String ssIs);

}
