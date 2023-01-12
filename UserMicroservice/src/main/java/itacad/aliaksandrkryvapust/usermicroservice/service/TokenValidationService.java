package itacad.aliaksandrkryvapust.usermicroservice.service;

import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.ValidationTokenMapper;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenValidationService {
    private final JwtTokenUtil jwtTokenUtil;
    private final ValidationTokenMapper validationTokenMapper;
    private final UserService userService;

    @Autowired
    public TokenValidationService(JwtTokenUtil jwtTokenUtil, ValidationTokenMapper validationTokenMapper, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.validationTokenMapper = validationTokenMapper;
        this.userService = userService;
    }

    public TokenValidationDto validateToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        String username = jwtTokenUtil.getUsername(token);
        User user = this.userService.getUser(username);
        return this.validationTokenMapper.outputMapping(user);
    }
}
