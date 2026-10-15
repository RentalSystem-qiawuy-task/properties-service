package fptu.semester8.mss301.propertiesservice.features.building.api;

import fptu.exe202.signify.apiresponse.exception.BadRequestException;
import fptu.exe202.signify.apiresponse.response.ApiResponse;
import fptu.semester8.mss301.propertiesservice.features.building.api.dto.BuildingRequest;
import fptu.semester8.mss301.propertiesservice.features.building.api.dto.BuildingResponse;
import fptu.semester8.mss301.propertiesservice.features.building.application.BuildingService;
import fptu.semester8.mss301.propertiesservice.features.building.mapper.BuildingMapper;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/buildings")
@Tag(name = "Buildings", description = "Building management")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BuildingController {
    static int MAX_PAGE_SIZE = 100;
    BuildingService buildingService;
    BuildingMapper buildingMapper;

    @PostMapping
    @Operation(summary = "Create a building")
    public ResponseEntity<ApiResponse<BuildingResponse>> create(@Valid @RequestBody BuildingRequest request) {
        BuildingResponse response = buildingMapper.toResponse(buildingService.create(buildingMapper.toCommand(request)));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{buildingId}")
                .buildAndExpand(response.buildingId())
                .toUri();
        return ResponseEntity.created(location).body(ApiResponse.success("Building created", response));
    }

    @GetMapping("/{buildingId}")
    @Operation(summary = "Get a building by ID")
    public ApiResponse<BuildingResponse> getById(@PathVariable long buildingId) {
        return ApiResponse.success(buildingMapper.toResponse(buildingService.getById(buildingId)));
    }

    @GetMapping
    @Operation(summary = "List buildings")
    public ApiResponse<Page<BuildingResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long wardId,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        if (page < 0 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new BadRequestException("page must be >= 0 and size must be between 1 and 100", "INVALID_PAGE");
        }
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        return ApiResponse.success(buildingService.list(wardId, pageable).map(buildingMapper::toResponse));
    }

    @PutMapping("/{buildingId}")
    @Operation(summary = "Update a building")
    public ApiResponse<BuildingResponse> update(@PathVariable long buildingId,
                                   @Valid @RequestBody BuildingRequest request) {
        return ApiResponse.success("Building updated", buildingMapper.toResponse(
                buildingService.update(buildingId, buildingMapper.toCommand(request))));
    }

    @DeleteMapping("/{buildingId}")
    @Operation(summary = "Soft-delete a building")
    public ApiResponse<Void> delete(@PathVariable long buildingId) {
        buildingService.delete(buildingId);
        return ApiResponse.success("Building deleted", null);
    }

    private Sort parseSort(String sort) {
        String[] parts = sort.split(",", 2);
        String property = parts[0].trim();
        if (!property.equals("createdAt") && !property.equals("updatedAt") && !property.equals("name")) {
            throw new BadRequestException("sort must be one of createdAt, updatedAt, or name", "INVALID_SORT");
        }
        Sort.Direction direction;
        try {
            direction = parts.length == 2
                    ? Sort.Direction.fromString(parts[1].trim())
                    : Sort.Direction.ASC;
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("sort direction must be asc or desc", "INVALID_SORT_DIRECTION");
        }
        return Sort.by(direction, property);
    }
}
