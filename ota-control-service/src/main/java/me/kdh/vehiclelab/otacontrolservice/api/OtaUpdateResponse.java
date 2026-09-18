package me.kdh.vehiclelab.otacontrolservice.api;

import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdate;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdateStatus;
import me.kdh.vehiclelab.otacontrolservice.projection.OtaUpdateProjection;

import java.time.LocalDateTime;

public record OtaUpdateResponse(
        Long id,
        String vehicleId,
        String targetVersion,
        OtaUpdateStatus status,
        int progress,
        LocalDateTime createdAt
) {
    public static OtaUpdateResponse from(OtaUpdate update) {
        return new OtaUpdateResponse(
                update.getId(),
                update.getVehicleId(),
                update.getTargetVersion(),
                update.getStatus(),
                update.getProgress(),
                update.getCreatedAt()
        );
    }

    public static OtaUpdateResponse from(OtaUpdateProjection projection) {
        return new OtaUpdateResponse(
                projection.getId(),
                projection.getVehicleId(),
                projection.getTargetVersion(),
                projection.getStatus(),
                projection.getProgress(),
                projection.getCreatedAt()
        );
    }
}
