package vn.ript.ssmanageadapter.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import vn.ript.ssmanageadapter.repository.AccessRequestRepository;
import vn.ript.ssmanageadapter.utils.CustomSpecification;
import vn.ript.ssmanageadapter.utils.Utils;
import vn.ript.ssmanageadapter.model.AccessRequest;

@Service
public class AccessRequestService {

    @Autowired
    AccessRequestRepository accessRequestRepository;

    public List<AccessRequest> findAll() {
        return accessRequestRepository.findAll();
    }

    public List<AccessRequest> findWithConditions(
            String fromId,
            String toId,
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
                .and(conditions.condition("to.ssId", toId, "equal"))
                .and(conditions.condition("from.ssId", fromId, "equal")), sort);
    }

    public List<AccessRequest> findSentWithConditions(
            String toId,
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
                .and(conditions.condition("to.ssId", toId, "equal"))
                .and(conditions.condition("from.ssId", Utils.SS_ID, "equal")), sort);
    }

    public List<AccessRequest> findReceivedWithConditions(
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
                .and(conditions.condition("to.ssId", Utils.SS_ID, "equal"))
                .and(conditions.condition("from.ssId", fromId, "equal")), sort);
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
