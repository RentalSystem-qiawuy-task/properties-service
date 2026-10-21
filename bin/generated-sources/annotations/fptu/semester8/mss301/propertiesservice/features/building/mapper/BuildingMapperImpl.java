package fptu.semester8.mss301.propertiesservice.features.building.mapper;

import fptu.semester8.mss301.propertiesservice.features.building.api.dto.BuildingRequest;
import fptu.semester8.mss301.propertiesservice.features.building.api.dto.BuildingResponse;
import fptu.semester8.mss301.propertiesservice.features.building.api.dto.BuildingSearchResponse;
import fptu.semester8.mss301.propertiesservice.features.building.application.command.BuildingCommand;
import fptu.semester8.mss301.propertiesservice.features.building.application.model.BuildingSearchItem;
import fptu.semester8.mss301.propertiesservice.features.building.domain.Building;
import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-21T18:15:12+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.200.v20261008-1115, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class BuildingMapperImpl implements BuildingMapper {

    @Override
    public BuildingResponse toResponse(Building building) {
        if ( building == null ) {
            return null;
        }

        Long buildingId = null;
        String name = null;
        String address = null;
        Long wardId = null;
        Long provinceId = null;
        BigDecimal latitude = null;
        BigDecimal longitude = null;
        String description = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        buildingId = building.getBuildingId();
        name = building.getName();
        address = building.getAddress();
        wardId = building.getWardId();
        provinceId = building.getProvinceId();
        latitude = building.getLatitude();
        longitude = building.getLongitude();
        description = building.getDescription();
        createdAt = building.getCreatedAt();
        updatedAt = building.getUpdatedAt();

        BuildingResponse buildingResponse = new BuildingResponse( buildingId, name, address, wardId, provinceId, latitude, longitude, description, createdAt, updatedAt );

        return buildingResponse;
    }

    @Override
    public BuildingCommand toCommand(BuildingRequest request) {
        if ( request == null ) {
            return null;
        }

        String name = null;
        String address = null;
        Long wardId = null;
        Long provinceId = null;
        BigDecimal latitude = null;
        BigDecimal longitude = null;
        String description = null;

        name = request.name();
        address = request.address();
        wardId = request.wardId();
        provinceId = request.provinceId();
        latitude = request.latitude();
        longitude = request.longitude();
        description = request.description();

        BuildingCommand buildingCommand = new BuildingCommand( name, address, wardId, provinceId, latitude, longitude, description );

        return buildingCommand;
    }

    @Override
    public BuildingSearchResponse toSearchResponse(BuildingSearchItem item) {
        if ( item == null ) {
            return null;
        }

        Long buildingId = null;
        String name = null;
        String address = null;
        Long provinceId = null;
        String provinceName = null;
        Long wardId = null;
        String wardName = null;
        BigDecimal latitude = null;
        BigDecimal longitude = null;
        String description = null;
        Instant createdAt = null;
        Instant updatedAt = null;
        Double distanceMeters = null;

        buildingId = item.buildingId();
        name = item.name();
        address = item.address();
        provinceId = item.provinceId();
        provinceName = item.provinceName();
        wardId = item.wardId();
        wardName = item.wardName();
        latitude = item.latitude();
        longitude = item.longitude();
        description = item.description();
        createdAt = item.createdAt();
        updatedAt = item.updatedAt();
        distanceMeters = item.distanceMeters();

        BuildingSearchResponse buildingSearchResponse = new BuildingSearchResponse( buildingId, name, address, provinceId, provinceName, wardId, wardName, latitude, longitude, description, createdAt, updatedAt, distanceMeters );

        return buildingSearchResponse;
    }
}
