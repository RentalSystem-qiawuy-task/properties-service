package fptu.semester8.mss301.propertiesservice.features.roomtype.mapper;

import fptu.semester8.mss301.propertiesservice.features.roomtype.api.dto.RoomTypeResponse;
import fptu.semester8.mss301.propertiesservice.features.roomtype.domain.RoomType;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-16T22:36:54+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class RoomTypeMapperImpl implements RoomTypeMapper {

    @Override
    public RoomTypeResponse toResponse(RoomType type) {
        if ( type == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = type.getId();
        name = type.getName();
        description = type.getDescription();
        createdAt = type.getCreatedAt();
        updatedAt = type.getUpdatedAt();

        RoomTypeResponse roomTypeResponse = new RoomTypeResponse( id, name, description, createdAt, updatedAt );

        return roomTypeResponse;
    }
}
