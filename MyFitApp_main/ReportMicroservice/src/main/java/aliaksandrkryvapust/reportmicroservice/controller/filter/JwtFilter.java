package aliaksandrkryvapust.reportmicroservice.controller.filter;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.TokenValidationDto;
import aliaksandrkryvapust.reportmicroservice.core.mapper.UserMapper;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.core.security.UserPrincipal;
import lombok.NonNull;
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

import static aliaksandrkryvapust.reportmicroservice.core.Constants.TOKEN_VERIFICATION_URI;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final UserMapper userMapper;

    public JwtFilter(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        TokenValidationDto validationDto = validateTokenThroughtRequest(requestTokenHeader);
        if (validationDto.getAuthenticated()) {
            this.prepareAuthenticationToken(request, validationDto);
            filterChain.doFilter(request, response);
        } else {
            logger.warn("Access denied");
        }
    }

    @NonNull
    private TokenValidationDto validateTokenThroughtRequest(String requestTokenHeader) {
        WebClient client = WebClient.create(TOKEN_VERIFICATION_URI);
        Mono<TokenValidationDto> resp = client.get()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, requestTokenHeader)
                .retrieve().bodyToMono(TokenValidationDto.class);
        return resp.blockOptional().orElseThrow();
    }

    private void prepareAuthenticationToken(HttpServletRequest request, TokenValidationDto validationDto) {
        UserPrincipal userPrincipal = this.userMapper.inputValidationMapping(validationDto);
        MyUserDetails userDetails = new MyUserDetails(userPrincipal);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}