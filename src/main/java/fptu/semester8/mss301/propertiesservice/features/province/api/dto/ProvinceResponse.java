package fptu.semester8.mss301.propertiesservice.features.province.api.dto;

import java.time.Instant;

public record ProvinceResponse(
        Long provinceId,
        String code,
        String name,
        String divisionType,
        Instant createdAt,
        Instant updatedAt) {
}
