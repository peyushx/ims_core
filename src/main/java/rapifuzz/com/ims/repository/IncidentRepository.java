package rapifuzz.com.ims.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rapifuzz.com.ims.model.entity.IncidentEntity;

public interface IncidentRepository extends JpaRepository<IncidentEntity,Long> {
    @Query(value = "SELECT * FROM incident where fk_id_user = ?1 and incident_number like %?2% ",
            countQuery = "SELECT count(*) FROM incident where incident_number like %?1%",
            nativeQuery = true)
    Page<IncidentEntity> findAll(Long id, String s, Pageable pageable);
}
