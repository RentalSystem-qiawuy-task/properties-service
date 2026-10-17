package fptu.semester8.mss301.propertiesservice.features.roomtype.api.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dữ liệu loại phòng")
public record RoomTypeRequest(
        @Schema(description = "Tên loại phòng, bắt buộc, tối đa 100 ký tự", example = "Studio") @NotBlank @Size(max = 100) String name,
        @Schema(description = "Mô tả loại phòng") String description
) {
}
