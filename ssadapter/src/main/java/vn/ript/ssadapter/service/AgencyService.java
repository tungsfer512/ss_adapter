package vn.ript.ssadapter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.model.Agency;
import vn.ript.ssadapter.repository.AgencyRepository;

@Service
public class AgencyService {

    @Autowired
    AgencyRepository agencyRepository;

    public List<Agency> getAgenciesList() {
        return agencyRepository.findAll();
    }

    public Agency registerAgency(Agency agency) {
        Agency existedAgency = agencyRepository.findAgencyByCode(agency.getCode());
        System.out.println("existedAgency: " + existedAgency);
        if (existedAgency != null) {
            agency.setId(existedAgency.getId());
            agency.setPid(existedAgency.getPid());
            existedAgency = agency;
            return agencyRepository.save(existedAgency);
        } else {
            return agencyRepository.save(agency);
        }
    }

    public void deleteAgencyByCode(String code) {
        agencyRepository.deleteAgencyByCode(code);
    }

    public Agency getAgencyByCode(String code) {
        return agencyRepository.findAgencyByCode(code);
    }

}
