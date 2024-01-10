package vn.ript.ssadapter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.ript.ssadapter.model.AccessRequest;

public interface AccessRequestRepository
                extends JpaRepository<AccessRequest, Integer>, JpaSpecificationExecutor<AccessRequest> {
        Optional<AccessRequest> findByExternalId(String externalId);
}
