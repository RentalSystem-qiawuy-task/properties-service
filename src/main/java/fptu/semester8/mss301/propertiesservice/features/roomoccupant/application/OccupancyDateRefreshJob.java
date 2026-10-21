package fptu.semester8.mss301.propertiesservice.features.roomoccupant.application;

import fptu.semester8.mss301.propertiesservice.features.room.application.RoomOccupancyStatusPolicy;
import fptu.semester8.mss301.propertiesservice.features.roomoccupant.infrastructure.persistence.ProjectionCheckpointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class OccupancyDateRefreshJob {
    private static final Logger log = LoggerFactory.getLogger(OccupancyDateRefreshJob.class);
    private final ProjectionCheckpointRepository checkpoints;
    private final RoomOccupancyStatusPolicy policy;

    // Fixed delay also repairs date transitions missed while the service was stopped.
    @Scheduled(fixedDelayString = "${property.occupancy.refresh-delay-ms:60000}")
    public void refreshDates() {
        long after = 0;
        while (true) {
            var ids = checkpoints.roomIdsAfter(after, 100);
            if (ids.isEmpty()) return;
            for (long roomId : ids) {
                try {
                    policy.refresh(roomId);
                } catch (RuntimeException failure) {
                    log.warn("Occupancy date refresh failed roomId={}; retry next run", roomId, failure);
                }
            }
            after = ids.getLast();
        }
    }
}
