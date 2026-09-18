package me.kdh.vehiclelab.otacontrolservice.application.query;

import me.kdh.vehiclelab.otacontrolservice.api.OtaUpdateResponse;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdateStatus;
import me.kdh.vehiclelab.otacontrolservice.projection.OtaUpdateProjection;
import me.kdh.vehiclelab.otacontrolservice.projection.OtaUpdateProjectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtaUpdateQueryServiceTests {

    @Mock
    private OtaUpdateProjectionRepository repository;

    @InjectMocks
    private OtaUpdateQueryService service;

    @Test
    void findAllReadsMongoProjectionInDescendingIdOrder() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 9, 18, 16, 0);
        when(repository.findAllByOrderByIdDesc()).thenReturn(List.of(
                new OtaUpdateProjection(
                        2L,
                        "VEHICLE-002",
                        "v1.0.2",
                        OtaUpdateStatus.PENDING,
                        0,
                        createdAt
                ),
                new OtaUpdateProjection(
                        1L,
                        "VEHICLE-001",
                        "v1.0.1",
                        OtaUpdateStatus.PENDING,
                        0,
                        createdAt.minusMinutes(1)
                )
        ));

        List<OtaUpdateResponse> responses = service.findAll();

        assertEquals(List.of(2L, 1L), responses.stream().map(OtaUpdateResponse::id).toList());
    }
}
