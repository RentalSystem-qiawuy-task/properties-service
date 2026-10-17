package fptu.semester8.mss301.propertiesservice.features.room.mapper;

import fptu.semester8.mss301.propertiesservice.features.room.api.dto.RoomResponse;
import fptu.semester8.mss301.propertiesservice.features.room.domain.Room;
import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-16T22:36:57+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class RoomMapperImpl implements RoomMapper {

    @Override
    public RoomResponse toResponse(Room room) {
        if ( room == null ) {
            return null;
        }

        Long id = null;
        Long buildingId = null;
        Long roomTypeId = null;
        String roomCode = null;
        Integer maxCapacity = null;
        String status = null;
        BigDecimal area = null;
        Integer bedroomCount = null;
        Integer bathroomCount = null;
        BigDecimal basePrice = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = room.getId();
        buildingId = room.getBuildingId();
        roomTypeId = room.getRoomTypeId();
        roomCode = room.getRoomCode();
        maxCapacity = room.getMaxCapacity();
        status = room.getStatus();
        area = room.getArea();
        bedroomCount = room.getBedroomCount();
        bathroomCount = room.getBathroomCount();
        basePrice = room.getBasePrice();
        createdAt = room.getCreatedAt();
        updatedAt = room.getUpdatedAt();

        RoomResponse roomResponse = new RoomResponse( id, buildingId, roomTypeId, roomCode, maxCapacity, status, area, bedroomCount, bathroomCount, basePrice, createdAt, updatedAt );

        return roomResponse;
    }
}
