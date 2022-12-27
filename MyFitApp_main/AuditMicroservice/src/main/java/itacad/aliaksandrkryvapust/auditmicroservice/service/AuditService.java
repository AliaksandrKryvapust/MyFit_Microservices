package itacad.aliaksandrkryvapust.auditmicroservice.service;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.api.IAuditRepository;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.service.api.IAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditService implements IAuditService {
    private final IAuditRepository auditRepository;

    @Autowired
    public AuditService(IAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public Audit save(Audit audit) {
        return this.auditRepository.save(audit);
    }

    @Override
    public Page<Audit> get(Pageable pageable) {
        return this.auditRepository.findAll(pageable);
    }

    @Override
    public Audit get(UUID id) {
        return this.auditRepository.findById(String.valueOf(id)).orElseThrow();
    }
}
