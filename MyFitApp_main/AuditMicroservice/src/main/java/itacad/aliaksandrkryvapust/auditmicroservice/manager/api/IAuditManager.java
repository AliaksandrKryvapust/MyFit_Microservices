package itacad.aliaksandrkryvapust.auditmicroservice.manager.api;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;

import java.util.List;
import java.util.UUID;

public interface IAuditManager extends IManager<AuditDto,AuditDtoOutput>{
    List<AuditDtoOutput> getByRecord(UUID id);
}
