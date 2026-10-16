package fptu.semester8.mss301.propertiesservice.features.room.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RoomRequest(
        @NotNull Long buildingId,
        @NotNull Long roomTypeId,
        @NotBlank @Size(max = 50) String roomCode,
        @NotNull @Min(1) Integer maxCapacity,
        @NotNull @DecimalMin(value = "0.01") BigDecimal area,
        @NotNull @Min(0) Integer bedroomCount,
        @NotNull @Min(0) Integer bathroomCount,
        @NotNull @DecimalMin(value = "0") BigDecimal basePrice,
        @NotNull String status
) {
}
