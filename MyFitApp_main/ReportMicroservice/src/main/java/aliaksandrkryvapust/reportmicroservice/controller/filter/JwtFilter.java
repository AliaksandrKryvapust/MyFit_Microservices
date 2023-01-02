package aliaksandrkryvapust.reportmicroservice.controller.filter;

import aliaksandrkryvapust.reportmicroservice.core.dto.TokenValidationDto;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.ArrayList;
import java.util.List;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.TOKEN_VERIFICATION_URI;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        WebClient client = WebClient.create(TOKEN_VERIFICATION_URI);
        Mono<TokenValidationDto> resp = client.get()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(AUTHORIZATION, requestTokenHeader)
                .retrieve().bodyToMono(TokenValidationDto.class);
        TokenValidationDto validationDto = resp.blockOptional().orElseThrow();
        if (validationDto.getAuthenticated()) {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority(validationDto.getRole().name()));
            UserDetails userDetails = new org.springframework.security.core.userdetails
                    .User(validationDto.getUsername(), "qwerty", authorityList);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } else {
            logger.warn("Access denied");
        }
    }
}