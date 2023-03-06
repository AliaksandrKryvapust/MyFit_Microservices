package itacad.aliaksandrkryvapust.auditmicroservice.service.transactional.api;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;

public interface IAuditTransactionalService {
    Audit saveTransactional(Audit audit);
}
