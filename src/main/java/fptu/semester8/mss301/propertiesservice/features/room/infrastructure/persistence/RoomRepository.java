package fptu.semester8.mss301.propertiesservice.features.room.infrastructure.persistence;

import fptu.semester8.mss301.propertiesservice.features.room.domain.Room;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByBuildingIdAndRoomCode(Long buildingId, String roomCode);

    boolean existsByBuildingIdAndRoomCodeAndIdNot(Long buildingId, String roomCode, Long id);

    Page<Room> findAllByBuildingId(Long buildingId, Pageable pageable);
}
