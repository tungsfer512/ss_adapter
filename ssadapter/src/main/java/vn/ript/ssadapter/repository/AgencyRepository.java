package vn.ript.ssadapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.ript.ssadapter.model.Agency;

public interface AgencyRepository extends JpaRepository<Agency, String> {

	Agency findAgencyByCode(@Param("code") String code);

	void deleteAgencyByCode(@Param("code") String code);
}
