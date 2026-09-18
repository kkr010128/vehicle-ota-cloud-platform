package me.kdh.vehiclelab.otacontrolservice.application.query;

import me.kdh.vehiclelab.otacontrolservice.api.OtaUpdateResponse;
import me.kdh.vehiclelab.otacontrolservice.projection.OtaUpdateProjectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtaUpdateQueryService {

    private final OtaUpdateProjectionRepository repository;

    public OtaUpdateQueryService(OtaUpdateProjectionRepository repository) {
        this.repository = repository;
    }

    public List<OtaUpdateResponse> findAll() {
        return repository.findAllByOrderByIdDesc()
                .stream()
                .map(OtaUpdateResponse::from)
                .toList();
    }
}
