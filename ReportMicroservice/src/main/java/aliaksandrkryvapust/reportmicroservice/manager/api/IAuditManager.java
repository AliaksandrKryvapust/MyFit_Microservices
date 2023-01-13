package aliaksandrkryvapust.reportmicroservice.manager.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.AuditDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URISyntaxException;

public interface IAuditManager {
    void audit(AuditDto auditDto) throws JsonProcessingException, URISyntaxException;
}
