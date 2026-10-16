package fptu.semester8.mss301.propertiesservice.features.building.application;

import fptu.exe202.signify.apiresponse.exception.ResourceNotFoundException;
import fptu.semester8.mss301.propertiesservice.common.id.IdGenerator;
import fptu.semester8.mss301.propertiesservice.features.building.application.exception.BuildingHasRoomsException;
import fptu.semester8.mss301.propertiesservice.features.building.application.exception.BuildingNotFoundException;
import fptu.semester8.mss301.propertiesservice.features.building.application.exception.WardNotFoundException;
import fptu.semester8.mss301.propertiesservice.features.building.application.command.BuildingCommand;
import fptu.semester8.mss301.propertiesservice.features.building.application.port.out.BuildingReferenceRepository;
import fptu.semester8.mss301.propertiesservice.features.building.application.port.out.BuildingRepository;
import fptu.semester8.mss301.propertiesservice.features.building.domain.Building;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BuildingService {

    BuildingRepository buildingRepository;

    BuildingReferenceRepository referenceRepository;

    IdGenerator idGenerator;

    @Transactional
    public Building create(BuildingCommand request) {

        if (referenceRepository.existWardById(request.wardId())) {
            throw new ResourceNotFoundException("Ward not exists");
        }

        if (referenceRepository.existProvinceById(request.provinceId())) {
            throw new ResourceNotFoundException("Province not exists");
        }
        Instant now = Instant.now();
        Building building = new Building(
                idGenerator.nextId(), request.name().trim(), request.address().trim(),
                request.wardId(), request.latitude(), request.longitude(),
                request.description(), now
        );
        return buildingRepository.save(building);
    }

    @Transactional(readOnly = true)
    public Building getById(long buildingId) {
        return findActive(buildingId);
    }

    @Transactional(readOnly = true)
    public Page<Building> list(Long wardId, Pageable pageable) {
        return wardId == null
                ? buildingRepository.findActive(pageable)
                : buildingRepository.findActiveByWardId(wardId, pageable);
    }

    @Transactional
    public Building update(long buildingId, BuildingCommand request) {
        Building building = findActive(buildingId);
        validateWard(request.wardId());
        building.update(
                request.name().trim(), request.address().trim(), request.wardId(),
                request.latitude(), request.longitude(), request.description(), Instant.now()
        );
        return buildingRepository.save(building);
    }

    @Transactional
    public void delete(long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new BuildingNotFoundException(buildingId));
        if (building.isDeleted()) {
            return;
        }
        if (referenceRepository.hasRooms(buildingId)) {
            throw new BuildingHasRoomsException(buildingId);
        }
        building.softDelete(Instant.now());
        buildingRepository.save(building);
    }

    private Building findActive(long buildingId) {
        return buildingRepository.findActiveById(buildingId)
                .orElseThrow(() -> new BuildingNotFoundException(buildingId));
    }

    private void validateWard(Long wardId) {
        if (!referenceRepository.wardExists(wardId)) {
            throw new WardNotFoundException(wardId);
        }
    }
}
