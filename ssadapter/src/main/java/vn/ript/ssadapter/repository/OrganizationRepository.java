package vn.ript.ssadapter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
    Optional<Organization> findByOrganId(@Param("organId") String organId);

    Optional<Organization> findBySsId(@Param("ssId") String ssIs);

    @Query(value = "SELECT * FROM _organization WHERE _organ_id<>:organId", nativeQuery = true)
    List<Organization> findAllExcept(@Param("organId") String organId);

    void deleteByOrganId(@Param("organId") String organId);

    void deleteBySsId(@Param("ssId") String ssId);
}
