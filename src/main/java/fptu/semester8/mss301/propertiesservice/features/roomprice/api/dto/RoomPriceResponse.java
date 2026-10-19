package fptu.semester8.mss301.propertiesservice.features.roomprice.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record RoomPriceResponse(
        Long id,
        Long roomId,
        BigDecimal price,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        Instant createdAt,
        Instant updatedAt
) {
}
