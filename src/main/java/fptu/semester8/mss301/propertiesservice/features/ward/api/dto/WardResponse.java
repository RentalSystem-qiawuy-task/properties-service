package fptu.semester8.mss301.propertiesservice.features.ward.api.dto;

import java.time.Instant;

public record WardResponse(
        Long wardId,
        Long provinceId,
        String code,
        String name,
        String divisionType,
        Instant createdAt,
        Instant updatedAt
) {
}
