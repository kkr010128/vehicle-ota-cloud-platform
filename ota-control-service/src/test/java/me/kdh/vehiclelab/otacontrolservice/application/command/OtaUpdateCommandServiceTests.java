package me.kdh.vehiclelab.otacontrolservice.application.command;

import me.kdh.vehiclelab.otacontrolservice.api.CreateOtaUpdateRequest;
import me.kdh.vehiclelab.otacontrolservice.api.OtaUpdateResponse;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdate;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdateRepository;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdateStatus;
import me.kdh.vehiclelab.otacontrolservice.projection.OtaUpdateProjectionSynchronizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtaUpdateCommandServiceTests {

    @Mock
    private OtaUpdateRepository repository;

    @Mock
    private OtaUpdateProjectionSynchronizer projectionSynchronizer;

    @InjectMocks
    private OtaUpdateCommandService service;

    @Test
    void createStoresCommandAndSynchronizesProjection() {
        when(repository.saveAndFlush(any(OtaUpdate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OtaUpdateResponse response = service.create(
                new CreateOtaUpdateRequest("VEHICLE-001", "v1.0.1")
        );

        assertEquals("VEHICLE-001", response.vehicleId());
        assertEquals("v1.0.1", response.targetVersion());
        assertEquals(OtaUpdateStatus.PENDING, response.status());
        assertEquals(0, response.progress());
        verify(projectionSynchronizer).synchronize(any(OtaUpdate.class));
    }
}
