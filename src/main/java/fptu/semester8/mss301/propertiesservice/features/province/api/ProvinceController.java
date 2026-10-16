package fptu.semester8.mss301.propertiesservice.features.province.api;

import fptu.exe202.signify.apiresponse.response.ApiResponse;
import fptu.semester8.mss301.propertiesservice.features.province.api.dto.*;
import fptu.semester8.mss301.propertiesservice.features.province.application.ProvinceService;
import fptu.semester8.mss301.propertiesservice.features.province.mapper.ProvinceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provinces")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceService service;
    private final ProvinceMapper mapper;

    @PostMapping
    public ApiResponse<ProvinceResponse> create(@Valid @RequestBody ProvinceRequest r) {
        return ApiResponse.success("Province created", mapper.toResponse(service.create(r.code(), r.name(), r.divisionType())));
    }

    @GetMapping
    public ApiResponse<List<ProvinceResponse>> list() {
        return ApiResponse.success(service.list().stream().map(mapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<ProvinceResponse> get(@PathVariable long id) {
        return ApiResponse.success(mapper.toResponse(service.get(id)));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProvinceResponse> update(@PathVariable long id, @Valid @RequestBody ProvinceRequest r) {
        return ApiResponse.success("Province updated", mapper.toResponse(service.update(id, r.code(), r.name(), r.divisionType())));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ApiResponse.success("Province deleted", null);
    }
}
