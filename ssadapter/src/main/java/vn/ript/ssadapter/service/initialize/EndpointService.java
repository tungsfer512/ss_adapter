package vn.ript.ssadapter.service.initialize;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.ript.ssadapter.model.initialize.Endpoint;
import vn.ript.ssadapter.repository.initialize.EndpointRepository;

@Service
public class EndpointService {

    @Autowired
    EndpointRepository endpointRepository;

    public List<Endpoint> findAll() {
        return endpointRepository.findAll();
    }

    public Endpoint save(Endpoint endpoint) {
        if (endpoint.getId() != null) {
            Optional<Endpoint> checkExistedEndpoint = endpointRepository.findById(endpoint.getId());
            if (checkExistedEndpoint.isPresent()) {
                Endpoint existedEndpoint = checkExistedEndpoint.get();
                endpoint.setId(existedEndpoint.getId());
                existedEndpoint = endpoint;
                return endpointRepository.save(existedEndpoint);
            } else {
                return endpointRepository.save(endpoint);
            }
        } else {
            return endpointRepository.save(endpoint);
        }
    }

    public void deleteById(Integer id) {
        endpointRepository.deleteById(id);
    }

    public Optional<Endpoint> findById(Integer id) {
        return endpointRepository.findById(id);
    }

    public Optional<Endpoint> findBySsId(String ssId) {
        return endpointRepository.findBySsId(ssId);
    }
}
