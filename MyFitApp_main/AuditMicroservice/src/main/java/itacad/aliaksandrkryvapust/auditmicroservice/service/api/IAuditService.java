package itacad.aliaksandrkryvapust.auditmicroservice.service.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;

import java.util.List;
import java.util.UUID;

public interface IAuditService extends IService<Audit>{
    List<Audit> getByRecord(UUID id);
}
