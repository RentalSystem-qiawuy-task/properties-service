package fptu.semester8.mss301.propertiesservice.features.building.infrastructure.persistence.adapter;

import fptu.semester8.mss301.propertiesservice.features.building.application.port.out.BuildingReferenceRepository;
import fptu.semester8.mss301.propertiesservice.features.province.infrastructure.persistence.repository.ProvinceRepository;
import fptu.semester8.mss301.propertiesservice.features.ward.infrastructure.persistence.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JdbcBuildingReferenceRepository implements BuildingReferenceRepository {

    JdbcTemplate jdbcTemplate;

    ProvinceRepository provinceRepository;

    WardRepository wardRepository;

    @Override
    public boolean wardExists(long wardId) {
        Boolean exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM ward WHERE ward_id = ?)",
                Boolean.class,
                wardId
        );
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public boolean hasRooms(long buildingId) {
        Boolean exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM room WHERE building_id = ?)",
                Boolean.class,
                buildingId
        );
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public boolean existProvinceById(long provinceId) {
        return provinceRepository.existsByProvinceId(provinceId);
    }

    @Override
    public boolean existWardById(long wardId) {
        return wardRepository.existsByWardId(wardId);
    }
}
