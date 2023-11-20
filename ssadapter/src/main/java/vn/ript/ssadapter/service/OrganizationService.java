package vn.ript.ssadapter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.model.Organization;
import vn.ript.ssadapter.repository.OrganizationRepository;

@Service
public class OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }

    public Organization add(Organization organization) {
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

    
    public void deleteByCode(String code) {
        organizationRepository.deleteByCode(code);
    }

    public Optional<Organization> findByCode(String code) {
        return organizationRepository.findByCode(code);
    }



}
