package fptu.semester8.mss301.propertiesservice.features.building.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record BuildingSearchResponse(Long buildingId, String name, String address,
                                     Long provinceId, String provinceName,
                                     Long wardId, String wardName,
                                     BigDecimal latitude, BigDecimal longitude,
                                     String description, Instant createdAt, Instant updatedAt,
                                     Double distanceMeters) {
}
