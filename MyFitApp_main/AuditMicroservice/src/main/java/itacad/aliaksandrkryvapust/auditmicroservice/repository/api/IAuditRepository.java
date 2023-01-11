package itacad.aliaksandrkryvapust.auditmicroservice.repository.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IAuditRepository extends JpaRepository<Audit, UUID> {
    Optional<Audit> findByUuid(UUID id);
}
