package itacad.aliaksandrkryvapust.auditmicroservice.service.api;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;

import java.util.List;
import java.util.UUID;

public interface IAuditManager extends IManager<AuditDto,AuditDtoOutput> {
    List<AuditDtoOutput> getActionDto(UUID id);
}
