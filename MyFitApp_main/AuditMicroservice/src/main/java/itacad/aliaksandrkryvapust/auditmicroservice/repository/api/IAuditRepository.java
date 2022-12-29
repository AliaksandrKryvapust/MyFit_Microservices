package itacad.aliaksandrkryvapust.auditmicroservice.repository.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuditRepository extends JpaRepository<Audit, String> {
}
