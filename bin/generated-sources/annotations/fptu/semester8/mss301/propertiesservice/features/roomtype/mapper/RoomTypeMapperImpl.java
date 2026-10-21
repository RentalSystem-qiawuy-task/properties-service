package fptu.semester8.mss301.propertiesservice.features.roomtype.mapper;

import fptu.semester8.mss301.propertiesservice.features.roomtype.api.dto.RoomTypeResponse;
import fptu.semester8.mss301.propertiesservice.features.roomtype.domain.RoomType;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-21T18:15:11+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.200.v20261008-1115, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class RoomTypeMapperImpl implements RoomTypeMapper {

    @Override
    public RoomTypeResponse toResponse(RoomType roomType) {
        if ( roomType == null ) {
            return null;
        }

        Long id = null;
        String typeCode = null;
        String name = null;
        String description = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = roomType.getId();
        typeCode = roomType.getTypeCode();
        name = roomType.getName();
        description = roomType.getDescription();
        createdAt = roomType.getCreatedAt();
        updatedAt = roomType.getUpdatedAt();

        RoomTypeResponse roomTypeResponse = new RoomTypeResponse( id, typeCode, name, description, createdAt, updatedAt );

        return roomTypeResponse;
    }
}
