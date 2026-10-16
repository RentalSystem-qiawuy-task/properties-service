package fptu.semester8.mss301.propertiesservice.features.roomtype.api.dto;

import java.time.Instant;

public record RoomTypeResponse(
        Long id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
