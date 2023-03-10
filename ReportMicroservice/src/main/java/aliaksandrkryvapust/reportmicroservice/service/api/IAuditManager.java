package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.AuditDto;

public interface IAuditManager {
    void audit(AuditDto auditDto);
}
