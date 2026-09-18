package me.kdh.vehiclelab.otacontrolservice.projection;

import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdate;
import org.springframework.stereotype.Component;

@Component
public class OtaUpdateProjectionSynchronizer {

    private final OtaUpdateProjectionRepository repository;

    public OtaUpdateProjectionSynchronizer(OtaUpdateProjectionRepository repository) {
        this.repository = repository;
    }

    public OtaUpdateProjection synchronize(OtaUpdate update) {
        return repository.save(OtaUpdateProjection.from(update));
    }
}
