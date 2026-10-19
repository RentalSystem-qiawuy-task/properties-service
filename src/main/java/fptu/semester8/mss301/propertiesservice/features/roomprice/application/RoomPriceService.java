package fptu.semester8.mss301.propertiesservice.features.roomprice.application;

import fptu.exe202.signify.apiresponse.exception.BadRequestException;
import fptu.exe202.signify.apiresponse.exception.ResourceNotFoundException;
import fptu.semester8.mss301.propertiesservice.common.id.IdGenerator;
import fptu.semester8.mss301.propertiesservice.features.room.infrastructure.persistence.RoomRepository;
import fptu.semester8.mss301.propertiesservice.features.roomprice.api.dto.SetRoomPriceRequest;
import fptu.semester8.mss301.propertiesservice.features.roomprice.domain.RoomPrice;
import fptu.semester8.mss301.propertiesservice.features.roomprice.infrastructure.RoomPriceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoomPriceService {

    RoomPriceRepository prices;

    RoomRepository rooms;

    IdGenerator ids;

    @Transactional
    public RoomPrice setPrice(long roomId, SetRoomPriceRequest request) {
        requireRoom(roomId);
        LocalDate today = LocalDate.now();
        if (request.effectiveFrom().isBefore(today)) {
            throw new BadRequestException("effectiveFrom must not be in the past", "ROOM_PRICE_EFFECTIVE_FROM_IN_PAST");
        }

        LocalDate effectiveFrom = request.effectiveFrom();
        Instant now = Instant.now();
        var existing = prices.findCoveringDate(roomId, effectiveFrom);
        if (existing.isPresent() && existing.get().getEffectiveFrom().isEqual(effectiveFrom)) {
            RoomPrice sameDate = existing.get();
            sameDate.changePrice(request.price(), now);
            return prices.save(sameDate);
        }

        existing.ifPresent(current -> {
            current.closeOn(effectiveFrom.minusDays(1), now);
            prices.saveAndFlush(current);
        });

        LocalDate effectiveTo = prices
                .findFirstByRoomIdAndEffectiveFromAfterOrderByEffectiveFromAsc(roomId, effectiveFrom)
                .map(next -> next.getEffectiveFrom().minusDays(1))
                .orElse(null);

        return prices.save(new RoomPrice(ids.nextId(), roomId, request.price(), effectiveFrom,
                effectiveTo, now));
    }

    @Transactional(readOnly = true)
    public RoomPrice getPriceForDate(long roomId, LocalDate date) {
        requireRoom(roomId);
        return prices.findCoveringDate(roomId, date).orElseThrow(() ->
                new ResourceNotFoundException("No room price for the requested date", "ROOM_PRICE_NOT_FOUND"));
    }

    @Transactional(readOnly = true)
    public List<RoomPrice> history(long roomId) {
        requireRoom(roomId);
        return prices.findAllByRoomIdOrderByEffectiveFromDesc(roomId);
    }

    private void requireRoom(long roomId) {
        if (!rooms.existsById(roomId)) {
            throw new ResourceNotFoundException("Room %d was not found".formatted(roomId), "ROOM_NOT_FOUND");
        }
    }
}
