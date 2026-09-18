package me.kdh.vehiclelab.otacontrolservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "ota_updates")
public class OtaUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false, length = 50)
    private String vehicleId;

    @Column(name = "target_version", nullable = false, length = 30)
    private String targetVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OtaUpdateStatus status;

    @Column(nullable = false)
    private int progress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected OtaUpdate() {
    }

    public OtaUpdate(String vehicleId, String targetVersion) {
        this.vehicleId = vehicleId;
        this.targetVersion = targetVersion;
        this.status = OtaUpdateStatus.PENDING;
        this.progress = 0;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
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