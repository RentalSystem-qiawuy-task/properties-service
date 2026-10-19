package fptu.semester8.mss301.propertiesservice.features.events;

import fptu.semester8.mss301.propertiesservice.features.room.application.RoomService;
import fptu.semester8.mss301.propertiesservice.features.room.domain.RoomStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ContractEventProcessorTest {
    private JdbcTemplate jdbc;
    private RoomService rooms;
    private ContractEventProcessor processor;

    @BeforeEach
    void setUp() {
        jdbc = mock(JdbcTemplate.class);
        rooms = mock(RoomService.class);
        processor = new ContractEventProcessor(new JsonMapper(), jdbc, rooms);
        when(jdbc.update(anyString(), any(UUID.class))).thenReturn(1);
    }

    @Test
    void signedOccupiesRoom() {
        processor.process(event("ContractSigned", ""));
        verify(rooms).changeStatusFromContract(12L, 34L, RoomStatus.OCCUPIED);
    }

    @Test
    void signedAcceptsLargeStringIdsWithoutPrecisionLoss() {
        String raw = event("ContractSigned", "").replace("\"contractId\":7,\"roomId\":12,\"buildingId\":34",
                "\"contractId\":\"898785309339156482\",\"roomId\":\"898785309339156480\",\"buildingId\":\"898785309339156481\"");
        processor.process(raw);
        verify(rooms).changeStatusFromContract(898785309339156480L, 898785309339156481L, RoomStatus.OCCUPIED);
    }

    @Test
    void moveOutApprovedMakesRoomUpcomingVacant() {
        processor.process(event("MoveOutApproved", ""));
        verify(rooms).changeStatusFromContract(12L, 34L, RoomStatus.UPCOMING_VACANT);
    }

    @Test
    void endedWithoutNextContractVacatesRoom() {
        processor.process(event("ContractEnded", ",\"nextContractExists\":false"));
        verify(rooms).changeStatusFromContract(12L, 34L, RoomStatus.VACANT);
    }

    @Test
    void endedWithNextContractKeepsRoomOccupied() {
        processor.process(event("ContractEnded", ",\"nextContractExists\":true"));
        verify(rooms).changeStatusFromContract(12L, 34L, RoomStatus.OCCUPIED);
        verify(rooms, never()).changeStatusFromContract(12L, 34L, RoomStatus.VACANT);
    }

    @Test
    void duplicateDoesNotChangeRoomAgain() {
        when(jdbc.update(anyString(), any(UUID.class))).thenReturn(1, 0);
        String raw = event("ContractSigned", "");
        processor.process(raw);
        processor.process(raw);
        verify(rooms, times(1)).changeStatusFromContract(12L, 34L, RoomStatus.OCCUPIED);
    }

    @Test
    void invalidEventFailsBeforeProcessedMarker() {
        assertThrows(InvalidEventException.class, () -> processor.process("{\"eventType\":\"ContractSigned\"}"));
        verifyNoInteractions(jdbc, rooms);
    }

    @Test
    void outOfRangeStringIdFailsBeforeProcessedMarker() {
        String raw = event("ContractSigned", "").replace("\"contractId\":7,\"roomId\":12,\"buildingId\":34",
                "\"contractId\":\"7\",\"roomId\":\"9223372036854775808\",\"buildingId\":\"34\"");
        assertThrows(InvalidEventException.class, () -> processor.process(raw));
        verifyNoInteractions(jdbc, rooms);
    }

    private static String event(String type, String extraPayloadFields) {
        return "{\"eventId\":\"%s\",\"eventType\":\"%s\",\"version\":1,"
                .formatted(UUID.randomUUID(), type)
                + "\"occurredAt\":\"2026-10-19T00:00:00Z\",\"payload\":{"
                + "\"contractId\":7,\"roomId\":12,\"buildingId\":34"
                + extraPayloadFields + "}}";
    }
}
