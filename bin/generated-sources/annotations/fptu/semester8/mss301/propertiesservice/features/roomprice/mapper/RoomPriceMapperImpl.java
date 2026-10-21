package fptu.semester8.mss301.propertiesservice.features.roomprice.mapper;

import fptu.semester8.mss301.propertiesservice.features.roomprice.api.dto.RoomPriceResponse;
import fptu.semester8.mss301.propertiesservice.features.roomprice.domain.RoomPrice;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-21T18:15:11+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.200.v20261008-1115, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class RoomPriceMapperImpl implements RoomPriceMapper {

    @Override
    public RoomPriceResponse toResponse(RoomPrice roomPrice) {
        if ( roomPrice == null ) {
            return null;
        }

        Long id = null;
        Long roomId = null;
        BigDecimal price = null;
        LocalDate effectiveFrom = null;
        LocalDate effectiveTo = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = roomPrice.getId();
        roomId = roomPrice.getRoomId();
        price = roomPrice.getPrice();
        effectiveFrom = roomPrice.getEffectiveFrom();
        effectiveTo = roomPrice.getEffectiveTo();
        createdAt = roomPrice.getCreatedAt();
        updatedAt = roomPrice.getUpdatedAt();

        RoomPriceResponse roomPriceResponse = new RoomPriceResponse( id, roomId, price, effectiveFrom, effectiveTo, createdAt, updatedAt );

        return roomPriceResponse;
    }
}
