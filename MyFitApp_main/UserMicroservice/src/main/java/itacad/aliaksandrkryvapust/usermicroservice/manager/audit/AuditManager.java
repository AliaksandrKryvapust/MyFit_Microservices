package itacad.aliaksandrkryvapust.usermicroservice.manager.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static itacad.aliaksandrkryvapust.usermicroservice.core.Constants.AUDIT_URI;
import static itacad.aliaksandrkryvapust.usermicroservice.core.Constants.TOKEN_HEADER;

@Component
public class AuditManager {
    private static final String jwtSecret = "NDQ1ZjAzNjQtMzViZi00MDRjLTljZjQtNjNjYWIyZTU5ZDYw";
    private final ObjectMapper objectMapper;

    @Autowired
    public AuditManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void audit(AuditDto auditDto) throws JsonProcessingException, URISyntaxException {
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(auditDto);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(AUDIT_URI))
                .header(TOKEN_HEADER, jwtSecret)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient.newHttpClient().sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}