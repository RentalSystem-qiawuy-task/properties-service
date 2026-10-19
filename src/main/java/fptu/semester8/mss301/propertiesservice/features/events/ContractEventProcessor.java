package fptu.semester8.mss301.propertiesservice.features.events;

import fptu.semester8.mss301.propertiesservice.features.room.application.RoomService;
import fptu.semester8.mss301.propertiesservice.features.room.domain.RoomStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContractEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(ContractEventProcessor.class);
    private static final Set<String> HANDLED = Set.of("ContractSigned", "MoveOutApproved", "ContractEnded");
    private final ObjectMapper json;
    private final JdbcTemplate jdbc;
    private final RoomService rooms;

    @Transactional
    public void process(String message) {
        JsonNode envelope;
        try {
            envelope = json.readTree(message);
        } catch (Exception invalidJson) {
            throw new InvalidEventException("Invalid contract event JSON", invalidJson);
        }
        if (envelope == null || !envelope.isObject()) {
            throw new InvalidEventException("Contract event envelope must be an object");
        }
        String eventType = requiredText(envelope, "eventType");
        if (!HANDLED.contains(eventType)) {
            log.debug("Ignoring unrelated contract eventType={}", eventType);
            return;
        }
        UUID eventId;
        try {
            eventId = UUID.fromString(requiredText(envelope, "eventId"));
            Instant.parse(requiredText(envelope, "occurredAt"));
        } catch (IllegalArgumentException invalid) {
            throw new InvalidEventException("Invalid eventId or occurredAt", invalid);
        }
        if (!envelope.path("version").canConvertToInt() || envelope.path("version").asInt() != 1) {
            throw new InvalidEventException("Unsupported contract event version");
        }
        JsonNode payload = envelope.path("payload");
        if (!payload.isObject()) throw new InvalidEventException("Missing contract event payload");
        long roomId = requiredLong(payload, "roomId");
        long buildingId = requiredLong(payload, "buildingId");
        requiredLong(payload, "contractId");
        boolean nextContractExists = false;
        if (eventType.equals("ContractEnded")) {
            if (!payload.path("nextContractExists").isBoolean()) {
                throw new InvalidEventException("Missing nextContractExists");
            }
            nextContractExists = payload.get("nextContractExists").asBoolean();
        }

        int inserted = jdbc.update("""
                INSERT INTO processed_event (event_id, processed_at)
                VALUES (?, CURRENT_TIMESTAMP) ON CONFLICT (event_id) DO NOTHING
                """, eventId);
        if (inserted == 0) {
            log.info("Duplicate eventId={} eventType={} aggregateId={} correlationId={}",
                    eventId, eventType, roomId, envelope.path("correlationId").asString(""));
            return;
        }
        RoomStatus status = switch (eventType) {
            case "ContractSigned" -> RoomStatus.OCCUPIED;
            case "MoveOutApproved" -> RoomStatus.UPCOMING_VACANT;
            case "ContractEnded" -> nextContractExists ? RoomStatus.OCCUPIED : RoomStatus.VACANT;
            default -> throw new IllegalStateException(eventType);
        };
        rooms.changeStatusFromContract(roomId, buildingId, status);
        log.info("Processed eventId={} eventType={} aggregateId={} correlationId={}",
                eventId, eventType, roomId, envelope.path("correlationId").asString(""));
    }

    private static String requiredText(JsonNode node, String field) {
        JsonNode value = node.path(field);
        if (!value.isTextual() || value.asString().isBlank()) {
            throw new InvalidEventException("Missing or invalid " + field);
        }
        return value.asString();
    }

    private static long requiredLong(JsonNode node, String field) {
        JsonNode value = node.path(field);
        if (value.isIntegralNumber() && value.canConvertToLong() && value.asLong() > 0) {
            return value.asLong();
        }
        if (value.isTextual()) {
            try {
                long id = Long.parseLong(value.asString());
                if (id > 0) return id;
            } catch (NumberFormatException ignored) {
                // Fall through to the consistent invalid-event error.
            }
        }
        throw new InvalidEventException("Missing or invalid " + field);
    }
}
