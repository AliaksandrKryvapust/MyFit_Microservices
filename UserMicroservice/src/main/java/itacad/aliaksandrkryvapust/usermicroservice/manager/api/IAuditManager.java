package itacad.aliaksandrkryvapust.usermicroservice.manager.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;

import java.net.URISyntaxException;

public interface IAuditManager {
    void audit(AuditDto auditDto) throws JsonProcessingException, URISyntaxException;
}
