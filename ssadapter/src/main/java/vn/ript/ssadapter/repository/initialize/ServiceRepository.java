package vn.ript.ssadapter.repository.initialize;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.initialize.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    Optional<Service> findBySsId(@Param("ssId") String ssIs);

}
