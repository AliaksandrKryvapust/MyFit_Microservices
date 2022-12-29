package itacad.aliaksandrkryvapust.myfitapp.service.security;

import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(user.getRole().name()));
        return this.tokenMapper.outputMapping(username, authorityList);
    }
}
