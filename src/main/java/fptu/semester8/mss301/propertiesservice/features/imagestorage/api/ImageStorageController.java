package fptu.semester8.mss301.propertiesservice.features.imagestorage.api;

import fptu.exe202.signify.apiresponse.response.ApiResponse;
import fptu.semester8.mss301.propertiesservice.features.imagestorage.application.ImageStorageService;
import fptu.semester8.mss301.propertiesservice.features.imagestorage.infrastructure.StorageObject;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ImageStorageController {

    ImageStorageService imageStorageService;

    @PostMapping(
            path = "/buildings/{buildingId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<StorageObject> uploadBuildingImage(
            @PathVariable long buildingId,
            @RequestPart("file") MultipartFile file
    ) {
        return ApiResponse.success("Building image uploaded", imageStorageService.upload("building", buildingId, file));
    }

    @PostMapping(
            path = "/rooms/{roomId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<StorageObject> uploadRoomImage(
            @PathVariable long roomId,
            @RequestPart("file") MultipartFile file
    ) {
        return ApiResponse.success("Room image uploaded", imageStorageService.upload("room", roomId, file));
    }

    @PostMapping(path = "/{folderName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<String>> uploadImages(
            @PathVariable String folderName,
            @RequestPart("files") MultipartFile[] files
    ) {
        return ApiResponse.success("Images uploaded", imageStorageService.upload(folderName, files));
    }
}
