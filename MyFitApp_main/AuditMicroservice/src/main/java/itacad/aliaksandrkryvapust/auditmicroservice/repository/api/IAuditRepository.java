package itacad.aliaksandrkryvapust.auditmicroservice.repository.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IAuditRepository extends JpaRepository<Audit, UUID> {
    List<Audit> findByUuid(UUID id);
}
