package itacad.aliaksandrkryvapust.auditmicroservice.manager;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.mapper.AuditMapper;
import itacad.aliaksandrkryvapust.auditmicroservice.manager.api.IAuditManager;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.service.api.IAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AuditManager implements IAuditManager {
    private final IAuditService auditService;
    private final AuditMapper auditMapper;

    @Autowired
    public AuditManager(IAuditService auditService, AuditMapper auditMapper) {
        this.auditService = auditService;
        this.auditMapper = auditMapper;
    }

    @Override
    public AuditDtoOutput save(AuditDto auditDto) {
        Audit audit = this.auditService.save(auditMapper.inputMapping(auditDto));
        return this.auditMapper.outputMapping(audit);
    }

    @Override
    public PageDtoOutput<AuditDtoOutput> get(Pageable pageable) {
        return this.auditMapper.outputPageMapping(auditService.get(pageable));
    }

    @Override
    public AuditDtoOutput get(UUID id) {
        return this.auditMapper.outputMapping(auditService.get(id));
    }

    @Override
    public List<AuditDtoOutput> getByRecord(UUID id) {
        return this.auditMapper.outputListMapping(auditService.getByRecord(id));
    }
}
