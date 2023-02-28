package itacad.aliaksandrkryvapust.productmicroservice.controller.filter;


import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.TokenValidationDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.UserMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static itacad.aliaksandrkryvapust.productmicroservice.core.Constants.TOKEN_HEADER;
import static itacad.aliaksandrkryvapust.productmicroservice.core.Constants.TOKEN_VERIFICATION_URI;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String jwtSecret = "NDQ1ZjAzNjQtMzViZi00MDRjLTljZjQtNjNjYWIyZTU5ZDYw";
    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (checkMicroserviceToken(request, response, filterChain)) return;
        checkJwtToken(request, response, filterChain);
    }

    private void checkJwtToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        TokenValidationDto validationDto = validateTokenThroughRequest(requestTokenHeader);
        if (validationDto.getAuthenticated()) {
            setAuthentication(validationDto, request, filterChain, response);
        }
    }

    private void setAuthentication(TokenValidationDto validationDto,
                                   HttpServletRequest request, FilterChain filterChain, HttpServletResponse response)
            throws IOException, ServletException {
        UserPrincipal userPrincipal = userMapper.inputValidationMapping(validationDto);
        MyUserDetails userDetails = new MyUserDetails(userPrincipal);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private TokenValidationDto validateTokenThroughRequest(String requestTokenHeader) {
        WebClient client = WebClient.create(TOKEN_VERIFICATION_URI);
        Mono<TokenValidationDto> resp = client.get()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, requestTokenHeader)
                .retrieve()
                .bodyToMono(TokenValidationDto.class);
        return resp.blockOptional().orElseThrow();
    }

    private boolean checkMicroserviceToken(HttpServletRequest request, HttpServletResponse response,
                                           FilterChain filterChain) throws IOException, ServletException {
        final String requestSecretHeader = request.getHeader(TOKEN_HEADER);
        TokenValidationDto validationDto = tokenMapper.createTokenValidationDto();
        if (requestSecretHeader != null) {
            if (requestSecretHeader.equals(jwtSecret)) {
                setAuthentication(validationDto, request, filterChain, response);
                return true;
            }
        }
        return false;
    }
}