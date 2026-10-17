package fptu.semester8.mss301.propertiesservice.features.room.api;

import fptu.exe202.signify.apiresponse.exception.BadRequestException;
import fptu.exe202.signify.apiresponse.response.ApiResponse;
import fptu.semester8.mss301.propertiesservice.features.room.api.dto.*;
import fptu.semester8.mss301.propertiesservice.features.room.application.RoomService;
import fptu.semester8.mss301.propertiesservice.features.room.mapper.RoomMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoomController {

    RoomService service;
    RoomMapper mapper;

    @PostMapping
    public ApiResponse<RoomResponse> create(@Valid @RequestBody RoomRequest r) {
        return ApiResponse.success("Room created", mapper.toResponse(service.create(r)));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoomResponse> get(@PathVariable long id) {
        return ApiResponse.success(mapper.toResponse(service.get(id)));
    }

    @GetMapping
    public ApiResponse<Page<RoomResponse>> list(@RequestParam(required = false) Long buildingId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "createdAt,desc") String sort) {
        if (page < 0 || size < 1 || size > 100) throw new BadRequestException("invalid pagination", "INVALID_PAGE");
        String[] s = sort.split(",", 2);
        if (!s[0].matches("createdAt|updatedAt|roomCode|status|basePrice"))
            throw new BadRequestException("invalid sort", "INVALID_SORT");
        return ApiResponse.success(service.list(buildingId, PageRequest.of(page, size, Sort.by(s.length > 1 && s[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, s[0]))).map(mapper::toResponse));
    }

    @GetMapping("/building/{buildingId}")
    public ApiResponse<Page<RoomResponse>> byBuilding(@PathVariable long buildingId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(service.list(buildingId, PageRequest.of(page, size)).map(mapper::toResponse));
    }

    @PutMapping("/{id}")
    public ApiResponse<RoomResponse> update(@PathVariable long id, @Valid @RequestBody RoomRequest r) {
        return ApiResponse.success("Room updated", mapper.toResponse(service.update(id, r)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ApiResponse.success("Room deleted", null);
    }

}
