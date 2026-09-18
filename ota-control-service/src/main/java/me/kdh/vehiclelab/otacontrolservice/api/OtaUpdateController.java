package me.kdh.vehiclelab.otacontrolservice.api;

import jakarta.validation.Valid;
import me.kdh.vehiclelab.otacontrolservice.application.OtaUpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ota-updates")
public class OtaUpdateController {

    private final OtaUpdateService service;

    public OtaUpdateController(OtaUpdateService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OtaUpdateResponse create(@Valid @RequestBody CreateOtaUpdateRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<OtaUpdateResponse> findAll() {
        return service.findAll();
    }
}