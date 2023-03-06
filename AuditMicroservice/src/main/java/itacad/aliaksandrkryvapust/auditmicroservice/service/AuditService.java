package itacad.aliaksandrkryvapust.auditmicroservice.service;

import itacad.aliaksandrkryvapust.auditmicroservice.repository.api.IAuditRepository;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.service.api.IAuditService;
import itacad.aliaksandrkryvapust.auditmicroservice.service.transactional.api.IAuditTransactionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService implements IAuditService {
    private final IAuditRepository auditRepository;
    private final IAuditTransactionalService transactionalService;

    @Override
    public Audit save(Audit audit) {
        return transactionalService.saveTransactional(audit);
    }

    @Override
    public Page<Audit> get(Pageable pageable) {
        return auditRepository.findAll(pageable);
    }

    @Override
    public Audit get(UUID id) {
        return auditRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Audit> getAction(UUID id) {
        return auditRepository.findByActionId(id);
    }
}
