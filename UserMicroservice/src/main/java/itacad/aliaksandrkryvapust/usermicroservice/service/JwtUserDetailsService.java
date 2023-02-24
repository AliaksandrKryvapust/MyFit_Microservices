package itacad.aliaksandrkryvapust.usermicroservice.service;

import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.TokenValidationDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.usermicroservice.repository.api.IUserRepository;
import itacad.aliaksandrkryvapust.usermicroservice.repository.cache.CacheStorage;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenManager;
import itacad.aliaksandrkryvapust.usermicroservice.service.validator.api.IUserDetailsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService, ITokenManager {
    private final IUserRepository userRepository;
    private final CacheStorage<Object> tokenBlackList;
    private final IUserDetailsValidator userDetailsValidator;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        boolean enabled = user.getStatus().equals(EUserStatus.ACTIVATED);
        boolean nonLocked = !user.getStatus().equals(EUserStatus.DEACTIVATED);
        Set<GrantedAuthority> authorityList = new HashSet<>();
        authorityList.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), enabled,
                true, true, nonLocked, authorityList);
    }

    public void logout(HttpServletRequest request) {
        String requestTokenHeader = request.getHeader(AUTHORIZATION);
        String jwtToken = requestTokenHeader.substring(7);
        tokenBlackList.add(jwtToken, new Object());
    }

    public UserLoginDtoOutput login(UserDtoLogin userDtoLogin) {
        UserDetails userDetails = loadUserByUsername(userDtoLogin.getEmail());
        userDetailsValidator.validateLogin(userDtoLogin, userDetails);
        String token = jwtTokenUtil.generateToken(userDetails);
        setLoginDate(userDetails);
        return userMapper.loginOutputMapping(userDetails, token);
    }

    public boolean tokenIsInBlackList(String token) {
        return tokenBlackList.get(token) != null;
    }

    public HttpCookie createJwtCookie(String token) {
        return jwtTokenUtil.createJwtCookie(token);
    }

    public UserDtoOutput getUserDto(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        return userMapper.outputMapping(user);
    }

    @Override
    public TokenValidationDto checkToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring("Bearer ".length());
        String username = jwtTokenUtil.getUsername(token);
        User user = userRepository.findByEmail(username).orElseThrow(NoSuchElementException::new);
        return tokenMapper.outputMapping(user);
    }

    private void setLoginDate(UserDetails userDetails) {
        User currentUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(NoSuchElementException::new);
        LocalDate date = LocalDate.now(ZoneOffset.UTC);
        currentUser.setDtLogin(date);
        userRepository.save(currentUser);
    }
}
