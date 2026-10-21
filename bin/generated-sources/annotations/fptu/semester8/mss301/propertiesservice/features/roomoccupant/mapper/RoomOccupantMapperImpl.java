package fptu.semester8.mss301.propertiesservice.features.roomoccupant.mapper;

import fptu.semester8.mss301.propertiesservice.features.roomoccupant.api.dto.RoomOccupantResponse;
import fptu.semester8.mss301.propertiesservice.features.roomoccupant.domain.RoomOccupant;
import fptu.semester8.mss301.propertiesservice.features.roomoccupant.domain.RoomOccupantStatus;
import java.time.Instant;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-21T18:15:12+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.200.v20261008-1115, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class RoomOccupantMapperImpl implements RoomOccupantMapper {

    @Override
    public RoomOccupantResponse toResponse(RoomOccupant occupant) {
        if ( occupant == null ) {
            return null;
        }

        Long id = null;
        Long contractTenantId = null;
        Long roomId = null;
        Long contractId = null;
        Long userId = null;
        LocalDate moveInDate = null;
        LocalDate moveOutDate = null;
        RoomOccupantStatus status = null;
        Long sourceRevision = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        id = occupant.getId();
        contractTenantId = occupant.getContractTenantId();
        roomId = occupant.getRoomId();
        contractId = occupant.getContractId();
        userId = occupant.getUserId();
        moveInDate = occupant.getMoveInDate();
        moveOutDate = occupant.getMoveOutDate();
        status = occupant.getStatus();
        sourceRevision = occupant.getSourceRevision();
        createdAt = occupant.getCreatedAt();
        updatedAt = occupant.getUpdatedAt();

        RoomOccupantResponse roomOccupantResponse = new RoomOccupantResponse( id, contractTenantId, roomId, contractId, userId, moveInDate, moveOutDate, status, sourceRevision, createdAt, updatedAt );

        return roomOccupantResponse;
    }
}
