package fptu.semester8.mss301.propertiesservice.features.room.application;

import fptu.exe202.signify.apiresponse.exception.*;
import fptu.semester8.mss301.propertiesservice.common.id.IdGenerator;
import fptu.semester8.mss301.propertiesservice.features.building.application.port.out.BuildingReferenceRepository;
import fptu.semester8.mss301.propertiesservice.features.room.api.dto.RoomRequest;
import fptu.semester8.mss301.propertiesservice.features.room.domain.*;
import fptu.semester8.mss301.propertiesservice.features.room.infrastructure.persistence.RoomRepository;
import fptu.semester8.mss301.propertiesservice.features.events.PropertyEventPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomService {

    RoomRepository rooms;

    BuildingReferenceRepository buildings;

    IdGenerator ids;

    PropertyEventPublisher events;

    @Transactional
    public Room create(RoomRequest r) {
        String status = normalizeStatus(r.status());
        validateBuilding(r.buildingId());
        if (rooms.existsByBuildingIdAndRoomCode(r.buildingId(), r.roomCode().trim()))
            throw new ConflictException("Room code already exists in building", "ROOM_CODE_CONFLICT");
        Instant n = Instant.now();
        Room room = rooms.saveAndFlush(new Room(ids.nextId(), r.buildingId(), r.roomTypeId(), r.roomCode().trim(), r.maxCapacity(), RoomStatus.VACANT.getValue(), r.area(), r.bedroomCount(), r.bathroomCount(), r.basePrice(), n));
        events.roomStatusChanged(room, null);
        return room;
    }

    @Transactional(readOnly = true)
    public Room get(long id) {
        return rooms.findById(id).orElseThrow(() -> new ResourceNotFoundException("Room %d was not found".formatted(id), "ROOM_NOT_FOUND"));
    }

    @Transactional(readOnly = true)
    public Page<Room> list(Long buildingId, Pageable p) {
        return buildingId == null ? rooms.findAll(p) : rooms.findAllByBuildingId(buildingId, p);
    }

    @Transactional
    public Room update(long id, RoomRequest r) {
        String status = normalizeStatus(r.status());
        Room x = get(id);
        validateBuilding(r.buildingId());
        String code = r.roomCode().trim();
        if (rooms.existsByBuildingIdAndRoomCodeAndIdNot(r.buildingId(), code, id))
            throw new ConflictException("Room code already exists in building", "ROOM_CODE_CONFLICT");
        if (!Objects.equals(x.getStatus(), status) && !allowed(x.getStatus(), status))
            throw new BadRequestException("Invalid room status transition", "INVALID_ROOM_STATUS_TRANSITION");
        String previousStatus = x.getStatus();
        x.update(r.roomTypeId(), code, r.maxCapacity(), status, r.area(), r.bedroomCount(), r.bathroomCount(), r.basePrice(), Instant.now());
        Room saved = rooms.saveAndFlush(x);
        if (!Objects.equals(previousStatus, status)) {
            events.roomStatusChanged(saved, previousStatus);
        }
        return saved;
    }

    @Transactional
    public void changeStatusFromContract(long roomId, long buildingId, RoomStatus status) {
        Room room = rooms.findByIdForUpdate(roomId).orElseThrow(() ->
                new ResourceNotFoundException("Room %d was not found".formatted(roomId), "ROOM_NOT_FOUND"));
        if (room.getBuildingId() != buildingId) {
            throw new BadRequestException("Contract event buildingId does not match room", "CONTRACT_ROOM_BUILDING_MISMATCH");
        }
        String previous = room.getStatus();
        if (previous.equals(status.getValue())) return;
        room.update(room.getRoomTypeId(), room.getRoomCode(), room.getMaxCapacity(),
                status.getValue(), room.getArea(), room.getBedroomCount(),
                room.getBathroomCount(), room.getBasePrice(), Instant.now());
        rooms.saveAndFlush(room);
        events.roomStatusChanged(room, previous);
    }

    @Transactional
    public void delete(long id) {
        rooms.delete(get(id));
    }

    private void validateBuilding(long id) {
        if (!buildings.buildingExists(id))
            throw new ResourceNotFoundException("Building %d was not found".formatted(id), "BUILDING_NOT_FOUND");
    }

    private String normalizeStatus(String status) {
        try {
            return RoomStatus.valueOf(status.trim().toUpperCase()).getValue();
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Unsupported room status", "INVALID_ROOM_STATUS");
        }
    }

    private boolean allowed(String a, String b) {
        return switch (a) {
            case "VACANT" -> Objects.equals(b, RoomStatus.OCCUPIED.getValue()) || Objects.equals(b, RoomStatus.MAINTENANCE.getValue());
            case "UPCOMING_VACANT" -> Objects.equals(b, RoomStatus.VACANT.getValue()) || Objects.equals(b, RoomStatus.OCCUPIED.getValue()) || Objects.equals(b, RoomStatus.MAINTENANCE.getValue());
            case "OCCUPIED" -> Objects.equals(b, RoomStatus.UPCOMING_VACANT.getValue()) || Objects.equals(b, RoomStatus.MAINTENANCE.getValue());
            case "MAINTENANCE" -> Objects.equals(b, RoomStatus.VACANT.getValue()) || Objects.equals(b, RoomStatus.UPCOMING_VACANT.getValue());
            default -> throw new ResourceNotFoundException("Unexpected value: " + a);
        };
    }
}
