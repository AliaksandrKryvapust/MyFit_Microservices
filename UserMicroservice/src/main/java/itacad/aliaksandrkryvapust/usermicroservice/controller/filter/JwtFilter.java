package itacad.aliaksandrkryvapust.usermicroservice.controller.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static itacad.aliaksandrkryvapust.usermicroservice.core.Constants.TOKEN_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private static final String jwtSecret = "NDQ1ZjAzNjQtMzViZi00MDRjLTljZjQtNjNjYWIyZTU5ZDYw";
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (this.validateSecret(request, response, filterChain)) return;
        this.validateJwtToken(request);
        filterChain.doFilter(request, response);
    }

    private void validateJwtToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.startsWithIgnoreCase(requestTokenHeader, "Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                if (jwtUserDetailsService.tokenIsInBlackList(jwtToken)){
                    return;
                }
                String username = jwtTokenUtil.getUsername(jwtToken);
                if (!username.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                    if (jwtTokenUtil.validate(jwtToken, userDetails)) {
                        setAuthentication(request, userDetails);
                    }
                }
            } catch (MalformedJwtException e) {
                log.error("JWT token is invalid" + e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token" + e.getMessage());
            } catch (ExpiredJwtException e) {
                log.error("JWT token is expired");
            } catch (IllegalArgumentException e) {
                log.error("Unable to fetch JWT token");
            }
        }
    }

    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private boolean validateSecret(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String requestSecretHeader = request.getHeader(TOKEN_HEADER);
        if (requestSecretHeader != null) {
            if (requestSecretHeader.equals(jwtSecret)) {
                Set<GrantedAuthority> authorityList = new HashSet<>();
                authorityList.add(new SimpleGrantedAuthority("APP"));
                UserDetails userDetails = new org.springframework.security.core.userdetails
                        .User("report@email", "report", authorityList);
                setAuthentication(request, userDetails);
                filterChain.doFilter(request, response);
                return true;
            }
        }
        return false;
    }
}
