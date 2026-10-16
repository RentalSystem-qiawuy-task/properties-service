package fptu.semester8.mss301.propertiesservice.features.ward.infrastructure.persistence.repository;

import fptu.semester8.mss301.propertiesservice.features.ward.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardRepository extends JpaRepository<Ward, Long> {

    boolean existsByWardId(Long wardId);
}
