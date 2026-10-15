package fptu.semester8.mss301.propertiesservice.features.building.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BuildingResponse(
        Long buildingId,
        String name,
        String address,
        Long wardId,
        BigDecimal latitude,
        BigDecimal longitude,
        String description,
        Instant createdAt,
        Instant updatedAt
) {}
