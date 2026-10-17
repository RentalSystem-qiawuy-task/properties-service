package fptu.semester8.mss301.propertiesservice.features.building.api.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dữ liệu tạo hoặc cập nhật tòa nhà")
public record BuildingRequest(
        @Schema(description = "Tên tòa nhà, bắt buộc, tối đa 200 ký tự", example = "Green Apartment") @NotBlank @Size(max = 200) String name,
        @Schema(description = "Địa chỉ, bắt buộc, tối đa 500 ký tự", example = "123 Nguyễn Huệ") @NotBlank @Size(max = 500) String address,
        @Schema(description = "ID phường/xã", example = "123456789") @NotNull Long wardId,
        @Schema(description = "ID tỉnh/thành phố", example = "987654321") @NotNull Long provinceId,
        @Schema(description = "Vĩ độ từ -90 đến 90", example = "10.7769") @NotNull @DecimalMin("-90") @DecimalMax("90") BigDecimal latitude,
        @Schema(description = "Kinh độ từ -180 đến 180", example = "106.7009") @NotNull @DecimalMin("-180") @DecimalMax("180") BigDecimal longitude,
        @Schema(description = "Mô tả tòa nhà") String description
) {}
