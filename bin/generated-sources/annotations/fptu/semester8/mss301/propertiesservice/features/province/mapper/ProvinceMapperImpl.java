package fptu.semester8.mss301.propertiesservice.features.province.mapper;

import fptu.semester8.mss301.propertiesservice.features.province.api.dto.ProvinceResponse;
import fptu.semester8.mss301.propertiesservice.features.province.domain.Province;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-16T22:36:57+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
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
