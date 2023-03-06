package itacad.aliaksandrkryvapust.auditmicroservice.repository.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IAuditRepository extends JpaRepository<Audit, UUID> {
    @Transactional(readOnly = true)
    List<Audit> findByActionId(UUID id);
}
