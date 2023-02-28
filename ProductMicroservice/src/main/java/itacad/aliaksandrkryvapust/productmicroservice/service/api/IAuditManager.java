package itacad.aliaksandrkryvapust.productmicroservice.service.api;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;

public interface IAuditManager {
    void audit(AuditDto auditDto);
}
