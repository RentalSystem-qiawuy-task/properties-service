package fptu.semester8.mss301.propertiesservice.features.roomtype.api.dto;

import jakarta.validation.constraints.*;

public record RoomTypeRequest(
        @NotBlank @Size(max = 100) String name,
        String description
) {
}
