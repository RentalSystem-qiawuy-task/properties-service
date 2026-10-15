package fptu.semester8.mss301.propertiesservice.features.building.application;

import fptu.semester8.mss301.propertiesservice.common.id.IdGenerator;
import fptu.semester8.mss301.propertiesservice.features.building.application.command.BuildingCommand;
import fptu.semester8.mss301.propertiesservice.features.building.application.exception.BuildingHasRoomsException;
import fptu.semester8.mss301.propertiesservice.features.building.application.exception.BuildingNotFoundException;
import fptu.semester8.mss301.propertiesservice.features.building.application.exception.WardNotFoundException;
import fptu.semester8.mss301.propertiesservice.features.building.domain.Building;
import fptu.semester8.mss301.propertiesservice.features.building.application.port.out.BuildingReferenceRepository;
import fptu.semester8.mss301.propertiesservice.features.building.application.port.out.BuildingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuildingServiceTest {
    private BuildingRepository buildingRepository;
    private BuildingReferenceRepository referenceRepository;
    private IdGenerator idGenerator;
    private BuildingService service;

    @BeforeEach
    void setUp() {
        buildingRepository = mock(BuildingRepository.class);
        referenceRepository = mock(BuildingReferenceRepository.class);
        idGenerator = mock(IdGenerator.class);
        service = new BuildingService(buildingRepository, referenceRepository, idGenerator);
    }

    @Test
    void createUsesApplicationIdAndValidatesWard() {
        when(referenceRepository.wardExists(7L)).thenReturn(true);
        when(idGenerator.nextId()).thenReturn(123L);
        when(buildingRepository.save(any(Building.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.create(request());

        assertEquals(123L, response.getBuildingId());
        assertEquals("Test building", response.getName());
        verify(referenceRepository).wardExists(7L);
        verify(idGenerator).nextId();
    }

    @Test
    void createRejectsUnknownWard() {
        when(referenceRepository.wardExists(7L)).thenReturn(false);

        assertThrows(WardNotFoundException.class, () -> service.create(request()));
        verify(buildingRepository, never()).save(any());
    }

    @Test
    void getByIdRejectsMissingOrDeletedBuilding() {
        when(buildingRepository.findActiveById(123L)).thenReturn(Optional.empty());

        assertThrows(BuildingNotFoundException.class, () -> service.getById(123L));
    }

    @Test
    void listUsesWardFilterAndPageable() {
        var pageable = PageRequest.of(0, 10);
        var building = building(123L);
        when(buildingRepository.findActiveByWardId(7L, pageable))
                .thenReturn(new PageImpl<>(List.of(building), pageable, 1));

        var result = service.list(7L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(123L, result.getContent().getFirst().getBuildingId());
    }

    @Test
    void updateChangesEditableFields() {
        var building = building(123L);
        when(buildingRepository.findActiveById(123L)).thenReturn(Optional.of(building));
        when(referenceRepository.wardExists(7L)).thenReturn(true);

        var response = service.update(123L, request());

        assertEquals("Test building", response.getName());
        assertEquals(new BigDecimal("10.00000000"), response.getLatitude());
        verify(buildingRepository).save(building);
    }

    @Test
    void deleteRejectsBuildingWithRooms() {
        when(buildingRepository.findById(123L)).thenReturn(Optional.of(building(123L)));
        when(referenceRepository.hasRooms(123L)).thenReturn(true);

        assertThrows(BuildingHasRoomsException.class, () -> service.delete(123L));
        verify(buildingRepository, never()).save(any());
    }

    @Test
    void deleteIsIdempotentForAlreadyDeletedBuilding() {
        var building = building(123L);
        building.softDelete(Instant.now());
        when(buildingRepository.findById(123L)).thenReturn(Optional.of(building));

        service.delete(123L);

        verify(referenceRepository, never()).hasRooms(123L);
        verify(buildingRepository, never()).save(any());
        assertTrue(building.isDeleted());
    }

    private BuildingCommand request() {
        return new BuildingCommand(" Test building ", " 123 Example Street ", 7L,
                new BigDecimal("10.00000000"), new BigDecimal("106.00000000"), "Description");
    }

    private Building building(long id) {
        Instant now = Instant.parse("2026-01-01T00:00:00Z");
        return new Building(id, "Old building", "Old address", 7L,
                new BigDecimal("10.00000000"), new BigDecimal("106.00000000"), null, now);
    }
}
