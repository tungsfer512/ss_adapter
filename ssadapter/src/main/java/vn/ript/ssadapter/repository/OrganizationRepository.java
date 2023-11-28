package vn.ript.ssadapter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, String> {
    Optional<Organization> findByOrganId(@Param("organId") String organId);

	void deleteByOrganId(@Param("organId") String organId);
}
