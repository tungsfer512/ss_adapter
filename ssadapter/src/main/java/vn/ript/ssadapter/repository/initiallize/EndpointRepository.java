package vn.ript.ssadapter.repository.initiallize;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.initialize.Endpoint;

public interface EndpointRepository extends JpaRepository<Endpoint, Integer> {
    Optional<Endpoint> findBySsId(@Param("ssId") String ssIs);

}
