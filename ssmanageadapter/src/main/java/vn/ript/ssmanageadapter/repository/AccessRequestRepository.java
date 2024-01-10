package vn.ript.ssmanageadapter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.ript.ssmanageadapter.model.AccessRequest;

public interface AccessRequestRepository
                extends JpaRepository<AccessRequest, Integer>, JpaSpecificationExecutor<AccessRequest> {
        Optional<AccessRequest> findByExternalId(String externalId);
}
