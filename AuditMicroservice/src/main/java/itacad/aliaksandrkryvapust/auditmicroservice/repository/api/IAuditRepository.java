package itacad.aliaksandrkryvapust.auditmicroservice.repository.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface IAuditRepository extends MongoRepository<Audit, UUID> {
    List<Audit> findByActionId(UUID id);
}
