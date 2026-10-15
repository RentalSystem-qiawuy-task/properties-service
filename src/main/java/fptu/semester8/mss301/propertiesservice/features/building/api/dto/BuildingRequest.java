package fptu.semester8.mss301.propertiesservice.features.building.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BuildingRequest(
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(max = 500) String address,
        @NotNull Long wardId,
        @NotNull @DecimalMin("-90") @DecimalMax("90") BigDecimal latitude,
        @NotNull @DecimalMin("-180") @DecimalMax("180") BigDecimal longitude,
        String description
) {}
