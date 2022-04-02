package liga.medical.medicalmonitoring.core.repository;

import liga.medical.medicalmonitoring.core.model.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEntityRepository extends JpaRepository<LogEntity, Long> {
}