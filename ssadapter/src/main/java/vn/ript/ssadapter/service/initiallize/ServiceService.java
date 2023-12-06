package vn.ript.ssadapter.service.initiallize;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import vn.ript.ssadapter.model.initialize.Service;
import vn.ript.ssadapter.repository.initiallize.ServiceRepository;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public Service save(Service service) {
        if (service.getId() != null) {
            Optional<Service> checkExistedService = serviceRepository.findById(service.getId());
            if (checkExistedService.isPresent()) {
                Service existedService = checkExistedService.get();
                service.setId(existedService.getId());
                existedService = service;
                return serviceRepository.save(existedService);
            } else {
                return serviceRepository.save(service);
            }
        } else {
            return serviceRepository.save(service);
        }
    }

    public void deleteById(Integer id) {
        serviceRepository.deleteById(id);
    }

    public Optional<Service> findById(Integer id) {
        return serviceRepository.findById(id);
    }

    public Optional<Service> findBySsId(String ssId) {
        return serviceRepository.findBySsId(ssId);
    }
}
