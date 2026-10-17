package fptu.semester8.mss301.propertiesservice.features.ward.api.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dữ liệu phường/xã")
public record WardRequest(
        @Schema(description = "ID tỉnh/thành trực thuộc", example = "123456789") @NotNull Long provinceId,
        @Schema(description = "Mã phường/xã, bắt buộc, tối đa 20 ký tự", example = "00001") @NotBlank @Size(max = 20) String code,
        @Schema(description = "Tên phường/xã, bắt buộc, tối đa 150 ký tự", example = "Phường Bến Nghé") @NotBlank @Size(max = 150) String name,
        @Schema(description = "Loại đơn vị hành chính, tối đa 100 ký tự", example = "Phường") @Size(max = 100) String divisionType
) {
}
