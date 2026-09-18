package me.kdh.vehiclelab.otacontrolservice.projection;

import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdate;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdateStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OtaUpdateProjectionSynchronizerTests {

    @Mock
    private OtaUpdateProjectionRepository repository;

    @InjectMocks
    private OtaUpdateProjectionSynchronizer synchronizer;

    @Test
    void synchronizeMapsCommandModelToMongoProjection() {
        OtaUpdate update = new OtaUpdate("VEHICLE-001", "v1.0.1");
        ArgumentCaptor<OtaUpdateProjection> captor = ArgumentCaptor.forClass(OtaUpdateProjection.class);

        synchronizer.synchronize(update);

        verify(repository).save(captor.capture());
        OtaUpdateProjection projection = captor.getValue();
        assertEquals("VEHICLE-001", projection.getVehicleId());
        assertEquals("v1.0.1", projection.getTargetVersion());
        assertEquals(OtaUpdateStatus.PENDING, projection.getStatus());
        assertEquals(0, projection.getProgress());
    }
}
