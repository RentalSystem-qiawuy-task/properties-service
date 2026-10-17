package fptu.semester8.mss301.propertiesservice.features.ward.api;

import fptu.exe202.signify.apiresponse.response.ApiResponse;
import fptu.semester8.mss301.propertiesservice.features.ward.api.dto.*;
import fptu.semester8.mss301.propertiesservice.features.ward.application.WardService;
import fptu.semester8.mss301.propertiesservice.features.ward.mapper.WardMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wards")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class WardController {

    WardService service;

    WardMapper mapper;

    @PostMapping
    public ApiResponse<WardResponse> create(@Valid @RequestBody WardRequest r) {
        return ApiResponse.success("Ward created", mapper.toResponse(service.create(r.provinceId(), r.code(), r.name(), r.divisionType())));
    }

    @GetMapping
    public ApiResponse<List<WardResponse>> list() {
        return ApiResponse.success(service.list().stream().map(mapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<WardResponse> get(@PathVariable long id) {
        return ApiResponse.success(mapper.toResponse(service.get(id)));
    }

    @PutMapping("/{id}")
    public ApiResponse<WardResponse> update(@PathVariable long id, @Valid @RequestBody WardRequest r) {
        return ApiResponse.success("Ward updated", mapper.toResponse(service.update(id, r.provinceId(), r.code(), r.name(), r.divisionType())));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ApiResponse.success("Ward deleted", null);
    }

    @GetMapping("/province/{provinceId}")
    public ApiResponse<List<WardResponse>> byProvince(@PathVariable long provinceId) {
        return ApiResponse.success(service.getWardsByProvinceId(provinceId).stream().map(mapper::toResponse).toList());
    }
}
