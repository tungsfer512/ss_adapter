package vn.ript.ssadapter.service.initiallize;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.model.initialize.ServiceDescription;
import vn.ript.ssadapter.repository.initiallize.ServiceDescriptionRepository;

@Service
public class ServiceDescriptionService {

    @Autowired
    ServiceDescriptionRepository serviceDescriptionRepository;

    public List<ServiceDescription> findAll() {
        return serviceDescriptionRepository.findAll();
    }

    public ServiceDescription save(ServiceDescription serviceDescription) {
        if (serviceDescription.getId() != null) {
            Optional<ServiceDescription> checkExistedServiceDescription = serviceDescriptionRepository.findById(serviceDescription.getId());
            if (checkExistedServiceDescription.isPresent()) {
                ServiceDescription existedServiceDescription = checkExistedServiceDescription.get();
                serviceDescription.setId(existedServiceDescription.getId());
                existedServiceDescription = serviceDescription;
                return serviceDescriptionRepository.save(existedServiceDescription);
            } else {
                return serviceDescriptionRepository.save(serviceDescription);
            }
        } else {
            return serviceDescriptionRepository.save(serviceDescription);
        }
    }

    public void deleteById(Integer id) {
        serviceDescriptionRepository.deleteById(id);
    }

    public Optional<ServiceDescription> findById(Integer id) {
        return serviceDescriptionRepository.findById(id);
    }

    public Optional<ServiceDescription> findBySsId(String ssId) {
        return serviceDescriptionRepository.findBySsId(ssId);
    }
}
