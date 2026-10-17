package fptu.semester8.mss301.propertiesservice.features.roomtype.mapper;
import fptu.semester8.mss301.propertiesservice.features.roomtype.api.dto.*; import fptu.semester8.mss301.propertiesservice.features.roomtype.domain.RoomType; import org.mapstruct.Mapper;
@Mapper(componentModel="spring") public interface RoomTypeMapper { RoomTypeResponse toResponse(RoomType type); }
