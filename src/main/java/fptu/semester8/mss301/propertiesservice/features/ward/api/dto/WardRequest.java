package fptu.semester8.mss301.propertiesservice.features.ward.api.dto;

import jakarta.validation.constraints.*;

public record WardRequest(
        @NotNull Long provinceId,
        @NotBlank @Size(max = 20) String code,
        @NotBlank @Size(max = 150) String name,
        @Size(max = 100) String divisionType
) {
}
