package me.kdh.vehiclelab.otacontrolservice.application.command;

import me.kdh.vehiclelab.otacontrolservice.api.CreateOtaUpdateRequest;
import me.kdh.vehiclelab.otacontrolservice.api.OtaUpdateResponse;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdate;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdateRepository;
import me.kdh.vehiclelab.otacontrolservice.projection.OtaUpdateProjectionSynchronizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OtaUpdateCommandService {

    private final OtaUpdateRepository repository;
    private final OtaUpdateProjectionSynchronizer projectionSynchronizer;

    public OtaUpdateCommandService(
            OtaUpdateRepository repository,
            OtaUpdateProjectionSynchronizer projectionSynchronizer
    ) {
        this.repository = repository;
        this.projectionSynchronizer = projectionSynchronizer;
    }

    @Transactional
    public OtaUpdateResponse create(CreateOtaUpdateRequest request) {
        OtaUpdate update = new OtaUpdate(request.vehicleId(), request.targetVersion());
        OtaUpdate saved = repository.saveAndFlush(update);
        projectionSynchronizer.synchronize(saved);
        return OtaUpdateResponse.from(saved);
    }
}
