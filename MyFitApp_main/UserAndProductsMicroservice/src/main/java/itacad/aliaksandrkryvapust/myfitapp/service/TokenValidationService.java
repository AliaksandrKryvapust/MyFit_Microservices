package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenValidationService {
    private final JwtTokenUtil jwtTokenUtil;
    private final TokenMapper tokenMapper;
    private final UserService userService;

    @Autowired
    public TokenValidationService(JwtTokenUtil jwtTokenUtil, TokenMapper tokenMapper, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenMapper = tokenMapper;
        this.userService = userService;
    }

    public TokenValidationDto validateToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        String username = jwtTokenUtil.getUsername(token);
        User user = this.userService.getUser(username);
        return this.tokenMapper.outputMapping(username, UserRole.valueOf(user.getRole().name()));
    }
}
