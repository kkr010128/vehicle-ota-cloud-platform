package me.kdh.vehiclelab.otacontrolservice.projection;

import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdate;
import me.kdh.vehiclelab.otacontrolservice.domain.OtaUpdateStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ota_update_projections")
public class OtaUpdateProjection {

    @Id
    private Long id;
    private String vehicleId;
    private String targetVersion;
    private OtaUpdateStatus status;
    private int progress;
    private LocalDateTime createdAt;

    protected OtaUpdateProjection() {
    }

    public OtaUpdateProjection(
            Long id,
            String vehicleId,
            String targetVersion,
            OtaUpdateStatus status,
            int progress,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.targetVersion = targetVersion;
        this.status = status;
        this.progress = progress;
        this.createdAt = createdAt;
    }

    public static OtaUpdateProjection from(OtaUpdate update) {
        return new OtaUpdateProjection(
                update.getId(),
                update.getVehicleId(),
                update.getTargetVersion(),
                update.getStatus(),
                update.getProgress(),
                update.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public OtaUpdateStatus getStatus() {
        return status;
    }

    public int getProgress() {
        return progress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
