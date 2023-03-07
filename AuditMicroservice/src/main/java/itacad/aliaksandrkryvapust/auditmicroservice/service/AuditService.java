package itacad.aliaksandrkryvapust.auditmicroservice.service;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.mapper.AuditMapper;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.api.IAuditRepository;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.auditmicroservice.service.api.IAuditService;
import itacad.aliaksandrkryvapust.auditmicroservice.service.validator.api.IAuditValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService implements IAuditService, IAuditManager {
    private final IAuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final IAuditValidator auditValidator;

    @Override
    public void save(Audit audit) {
        auditRepository.insert(audit);
    }

    @Override
    public Page<Audit> get(Pageable pageable) {
        return auditRepository.findAll(pageable);
    }

    @Override
    public List<Audit> getAction(UUID id) {
        return auditRepository.findByActionId(id);
    }

    @Override
    public void saveDto(AuditDto auditDto) {
        Audit entityToSave = auditMapper.inputMapping(auditDto);
        auditValidator.validateEntity(entityToSave);
        save(entityToSave);
    }

    @Override
    public PageDtoOutput<AuditDtoOutput> getDto(Pageable pageable) {
        Page<Audit> page = get(pageable);
        return auditMapper.outputPageMapping(page);
    }

    @Override
    public List<AuditDtoOutput> getActionDto(UUID id) {
        List<Audit> action = getAction(id);
        return auditMapper.outputListMapping(action);
    }
}
