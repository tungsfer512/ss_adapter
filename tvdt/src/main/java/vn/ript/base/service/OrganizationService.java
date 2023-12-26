package vn.ript.base.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.base.model.Organization;
import vn.ript.base.repository.OrganizationRepository;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public List<Organization> findAllExcept(String organId) {
        return organizationRepository.findAllExcept(organId);
    }

    public Organization save(Organization organization) {
        if (organization.getId() != null && !organization.getId().equalsIgnoreCase("")) {
            Optional<Organization> checkExistedOrganization = organizationRepository.findById(organization.getId());
            if (checkExistedOrganization.isPresent()) {
                Organization existedOrganization = checkExistedOrganization.get();
                organization.setId(existedOrganization.getId());
                existedOrganization = organization;
                return organizationRepository.save(existedOrganization);
            } else {
                return organizationRepository.save(organization);
            }
        } else {
            return organizationRepository.save(organization);
        }
    }

    public void deleteById(String id) {
        organizationRepository.deleteById(id);
    }

    public Optional<Organization> findById(String id) {
        return organizationRepository.findById(id);
    }

    
    public void deleteByOrganId(String organId) {
        organizationRepository.deleteByOrganId(organId);
    }

    public Optional<Organization> findByOrganId(String organId) {
        return organizationRepository.findByOrganId(organId);
    }

    public Optional<Organization> findBySsId(String ssId) {
        return organizationRepository.findBySsId(ssId);
    }

}
