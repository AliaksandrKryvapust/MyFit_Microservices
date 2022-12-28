package itacad.aliaksandrkryvapust.auditmicroservice.controller.filter;


import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.TokenValidationDto;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static itacad.aliaksandrkryvapust.auditmicroservice.core.Constants.TOKEN_HEADER;
import static itacad.aliaksandrkryvapust.auditmicroservice.core.Constants.TOKEN_VERIFICATION_URI;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final String jwtSecret = "NDQ1ZjAzNjQtMzViZi00MDRjLTljZjQtNjNjYWIyZTU5ZDYw";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String requestSecretHeader = request.getHeader(TOKEN_HEADER);
        if (requestSecretHeader != null) {
            if (requestSecretHeader.equals(jwtSecret)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        WebClient client = WebClient.create(TOKEN_VERIFICATION_URI);
        TokenValidationDto resp = client.get()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, requestTokenHeader)
                .exchangeToMono((r) -> {
                    if (r.statusCode().equals(HttpStatus.OK)) {
                        return r.bodyToMono(TokenValidationDto.class);
                    } else if (r.statusCode().is4xxClientError()) {
                        throw new DataIntegrityViolationException("Bad request");
                    } else {
                        throw new RuntimeException("Failed to get token data");
                    }
                }).block();
        if (resp != null) {
            if (resp.getIsAuthenticated()) {
                filterChain.doFilter(request, response);
            } else {
                logger.warn("Access denied");
            }
        }
    }
}