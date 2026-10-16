package fptu.semester8.mss301.propertiesservice.features.room.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record RoomResponse(
        Long id,
        Long buildingId,
        Long roomTypeId,
        String roomCode,
        Integer maxCapacity,
        String status,
        BigDecimal area,
        Integer bedroomCount,
        Integer bathroomCount,
        BigDecimal basePrice, Instant createdAt,
        Instant updatedAt
) {
}
