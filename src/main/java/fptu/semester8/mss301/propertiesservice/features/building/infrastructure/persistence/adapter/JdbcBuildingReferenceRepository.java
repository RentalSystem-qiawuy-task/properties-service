package fptu.semester8.mss301.propertiesservice.features.building.infrastructure.persistence.adapter;

import fptu.semester8.mss301.propertiesservice.features.building.application.port.out.BuildingReferenceRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcBuildingReferenceRepository implements BuildingReferenceRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcBuildingReferenceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
}
