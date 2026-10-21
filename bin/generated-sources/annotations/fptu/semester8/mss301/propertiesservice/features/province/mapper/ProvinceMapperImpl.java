package fptu.semester8.mss301.propertiesservice.features.province.mapper;

import fptu.semester8.mss301.propertiesservice.features.province.api.dto.ProvinceResponse;
import fptu.semester8.mss301.propertiesservice.features.province.domain.Province;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-21T18:15:12+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.200.v20261008-1115, environment: Java 25.0.4.1 (Eclipse Adoptium)"
)
@Component
public class ProvinceMapperImpl implements ProvinceMapper {

    @Override
    public ProvinceResponse toResponse(Province province) {
        if ( province == null ) {
            return null;
        }

        Long provinceId = null;
        String code = null;
        String name = null;
        String divisionType = null;
        Instant createdAt = null;
        Instant updatedAt = null;

        provinceId = province.getProvinceId();
        code = province.getCode();
        name = province.getName();
        divisionType = province.getDivisionType();
        createdAt = province.getCreatedAt();
        updatedAt = province.getUpdatedAt();

        ProvinceResponse provinceResponse = new ProvinceResponse( provinceId, code, name, divisionType, createdAt, updatedAt );

        return provinceResponse;
    }
}
