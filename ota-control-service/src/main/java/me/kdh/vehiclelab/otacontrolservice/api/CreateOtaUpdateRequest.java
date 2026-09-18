package me.kdh.vehiclelab.otacontrolservice.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOtaUpdateRequest(
        @NotBlank @Size(max = 50) String vehicleId,
        @NotBlank @Size(max = 30) String targetVersion
) {
}