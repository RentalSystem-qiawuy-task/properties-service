package fptu.semester8.mss301.propertiesservice.features.ward.mapper;

import fptu.semester8.mss301.propertiesservice.features.ward.api.dto.WardResponse;
import fptu.semester8.mss301.propertiesservice.features.ward.domain.Ward;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-21T18:15:12+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.200.v20261008-1115, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class WardMapperImpl implements WardMapper {

    @Override
    public WardResponse toResponse(Ward ward) {
        if ( ward == null ) {
            return null;
        }

        Long wardId = null;
        Long provinceId = null;
        String code = null;
        String name = null;
        String divisionType = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        wardId = ward.getWardId();
        provinceId = ward.getProvinceId();
        code = ward.getCode();
        name = ward.getName();
        divisionType = ward.getDivisionType();
        createdAt = ward.getCreatedAt();
        updatedAt = ward.getUpdatedAt();

        WardResponse wardResponse = new WardResponse( wardId, provinceId, code, name, divisionType, createdAt, updatedAt );

        return wardResponse;
    }
}
