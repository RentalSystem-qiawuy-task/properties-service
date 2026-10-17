package fptu.semester8.mss301.propertiesservice.features.roomtype.api;

import fptu.exe202.signify.apiresponse.response.ApiResponse;
import fptu.semester8.mss301.propertiesservice.features.roomtype.api.dto.*;
import fptu.semester8.mss301.propertiesservice.features.roomtype.application.RoomTypeService;
import fptu.semester8.mss301.propertiesservice.features.roomtype.mapper.RoomTypeMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room-types")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RoomTypeController {

    RoomTypeService service;

    RoomTypeMapper mapper;

    @PostMapping
    public ApiResponse<RoomTypeResponse> create(@Valid @RequestBody RoomTypeRequest r) {
        return ApiResponse.success("Room type created", mapper.toResponse(service.create(r)));
    }

    @GetMapping("/{id}")
    public ApiResponse<RoomTypeResponse> get(@PathVariable long id) {
        return ApiResponse.success(mapper.toResponse(service.get(id)));
    }

    @GetMapping
    public ApiResponse<Page<RoomTypeResponse>> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(service.list(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))).map(mapper::toResponse));
    }

    @PutMapping("/{id}")
    public ApiResponse<RoomTypeResponse> update(@PathVariable long id, @Valid @RequestBody RoomTypeRequest r) {
        return ApiResponse.success("Room type updated", mapper.toResponse(service.update(id, r)));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ApiResponse.success("Room type deleted", null);
    }
}
