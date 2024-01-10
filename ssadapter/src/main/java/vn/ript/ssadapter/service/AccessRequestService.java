package vn.ript.ssadapter.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.repository.AccessRequestRepository;
import vn.ript.ssadapter.utils.CustomSpecification;
import vn.ript.ssadapter.model.AccessRequest;

@Service
public class AccessRequestService {

    @Autowired
    AccessRequestRepository accessRequestRepository;

    public List<AccessRequest> findAll() {
        return accessRequestRepository.findAll();
    }

    public List<AccessRequest> findWithConditions(
            String fromId,
            String type,
            String status,
            String sortCreateAt) {
        CustomSpecification<AccessRequest> conditions = new CustomSpecification<>();
        List<Order> orders = new ArrayList<>();
        if (sortCreateAt != null) {
            if (sortCreateAt.equalsIgnoreCase("ASC")) {
                orders.add(new Order(Sort.Direction.ASC, "createAt"));
            } else if (sortCreateAt.equalsIgnoreCase("DESC")) {
                orders.add(new Order(Sort.Direction.DESC, "createAt"));
            }
        }
        Sort sort = Sort.by(orders);

        return accessRequestRepository.findAll(conditions
                .and(conditions.condition("status", status, "equal"))
                .and(conditions.condition("type", type, "equal"))
                .and(conditions.condition("from.organId", fromId, "equal")), sort);
    }

    public Optional<AccessRequest> findById(Integer id) {
        return accessRequestRepository.findById(id);
    }

    public Optional<AccessRequest> findByExternalId(String externalId) {
        return accessRequestRepository.findByExternalId(externalId);
    }

    public AccessRequest saveAccessRequest(AccessRequest accessRequest) {
        return accessRequestRepository.save(accessRequest);
    }

}
