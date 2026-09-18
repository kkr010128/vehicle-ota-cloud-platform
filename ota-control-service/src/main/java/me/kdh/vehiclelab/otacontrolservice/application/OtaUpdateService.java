package me.kdh.vehiclelab.otacontrolservice.application;

import me.kdh.vehiclelab.otacontrolservice.api.CreateOtaUpdateRequest;
import me.kdh.vehiclelab.otacontrolservice.api.OtaUpdateResponse;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdate;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdateRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OtaUpdateService {

    private final OtaUpdateRepository repository;

    public OtaUpdateService(OtaUpdateRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public OtaUpdateResponse create(CreateOtaUpdateRequest request) {
        OtaUpdate update = new OtaUpdate(request.vehicleId(), request.targetVersion());
        return OtaUpdateResponse.from(repository.save(update));
    }

    public List<OtaUpdateResponse> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(OtaUpdateResponse::from)
                .toList();
    }
}