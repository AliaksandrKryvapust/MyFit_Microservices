package itacad.aliaksandrkryvapust.productmicroservice.manager.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IAuditManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static itacad.aliaksandrkryvapust.productmicroservice.core.Constants.AUDIT_URI;
import static itacad.aliaksandrkryvapust.productmicroservice.core.Constants.TOKEN_HEADER;

@Component
public class AuditManager implements IAuditManager {
    private static final String jwtSecret = "NDQ1ZjAzNjQtMzViZi00MDRjLTljZjQtNjNjYWIyZTU5ZDYw";
    private final ObjectMapper objectMapper;

    @Autowired
    public AuditManager(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void audit(AuditDto auditDto) throws JsonProcessingException, URISyntaxException {
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(auditDto);
        HttpRequest httpRequest = prepareRequest(requestBody);
        HttpClient.newHttpClient().sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest prepareRequest(String requestBody) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(AUDIT_URI))
                .header(TOKEN_HEADER, jwtSecret)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }
}
