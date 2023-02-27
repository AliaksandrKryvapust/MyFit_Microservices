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
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.validator.api.IUserDetailsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil.JWT_TOKEN_VALID_TIME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {
    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private IUserDetailsValidator userDetailsValidator;
    @Mock
    private UserMapper userMapper;
    @Mock
    private TokenMapper tokenMapper;
    @Mock
    private CacheStorage<Object> tokenBlackList;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final String username = "someone";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String password = "kdrL556D";
    final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    final LocalDate dtLogin = LocalDate.parse("04/04/2023", df);
    final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBteWZpdC5jb20iLCJpYXQiOjE2NzM1MzE5MzEsImV4cCI6MTY3MzUzNTUzMX0.ncZiUNsJK1LFh2U59moFgWhzcWZyW3p0TL9O_hWVcvw";

    @Test
    void loadUserByUsername() {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userOutput));

        //test
        UserDetails actual = jwtUserDetailsService.loadUserByUsername(email);

        // assert
        assertNotNull(actual);
        assertNotNull(actual.getAuthorities());
        assertEquals(email, actual.getUsername());
        assertEquals(password, actual.getPassword());
        assertEquals(1, actual.getAuthorities().size());
    }

    @Test
    void login() {
        // preconditions
        final UserDtoLogin dtoInput = getPreparedUserDtoLogin();
        final UserLoginDtoOutput dtoOutput = getPreparedUserLoginDtoOutput();
        final User userOutput = getPreparedUserOutput();
        final UserDetails userDetails = getPreparedUserDetails();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userOutput));
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userOutput));
        when(userMapper.loginOutputMapping(userDetails, token)).thenReturn(dtoOutput);
        ArgumentCaptor<UserDtoLogin> actualUserDtoLogin = ArgumentCaptor.forClass(UserDtoLogin.class);
        ArgumentCaptor<UserDetails> actualUserDetails = ArgumentCaptor.forClass(UserDetails.class);

        //test
        UserLoginDtoOutput actual = jwtUserDetailsService.login(dtoInput);
        Mockito.verify(userDetailsValidator, Mockito.times(1)).validateLogin(actualUserDtoLogin.capture(),
                actualUserDetails.capture());
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));

        // assert
        assertNotNull(actual);
        assertEquals(email, actual.getEmail());
        assertEquals(token, actual.getToken());
    }

    @Test
    void logout() {
        // preconditions
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn(token);

        //test
        jwtUserDetailsService.logout(request);
        Mockito.verify(tokenBlackList, Mockito.times(1)).add(ArgumentMatchers.any(String.class),
                ArgumentMatchers.any(Object.class));
    }

    @Test
    void tokenIsInBlackList() {
        // preconditions
        when(tokenBlackList.get(token)).thenReturn(token);
        ArgumentCaptor<String> actualToken = ArgumentCaptor.forClass(String.class);

        //test
        boolean actual = jwtUserDetailsService.tokenIsInBlackList(token);
        Mockito.verify(tokenBlackList, Mockito.times(1)).get(actualToken.capture());

        // assert
        assertEquals(token, actualToken.getValue());
        assertTrue(actual);
    }

    @Test
    void createJwtCookie() {
        // preconditions
        ResponseCookie cookie = getPreparedCookie();
        when(jwtTokenUtil.createJwtCookie(token)).thenReturn(cookie);

        //test
        HttpCookie actual = jwtUserDetailsService.createJwtCookie(token);

        // assert
        assertEquals("Bearer+" + token, actual.getValue());
    }

    @Test
    void getUserDto() {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        final UserDtoOutput dtoOutput = getPreparedUserDtoOutput();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userOutput));
        when(userMapper.outputMapping(userOutput)).thenReturn(dtoOutput);

        //test
        UserDtoOutput actual = jwtUserDetailsService.getUserDto(email);

        // assert
        assertNotNull(actual);
        checkUserDtoOutputFields(actual);
    }

    @Test
    void checkToken() {
        // preconditions
        final User userOutput = getPreparedUserOutput();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final TokenValidationDto validationDto = getPreparedTokenValidationDto();
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtTokenUtil.getUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userOutput));
        when(tokenMapper.outputMapping(userOutput)).thenReturn(validationDto);

        //test
        TokenValidationDto actual = jwtUserDetailsService.checkToken(request);

        // assert
        assertNotNull(actual);
        checkTokenValidationOutputFields(actual);
    }

    User getPreparedUserOutput() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .username(username)
                .role(EUserRole.USER)
                .status(EUserStatus.ACTIVATED)
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .build();
    }

    UserDtoLogin getPreparedUserDtoLogin() {
        return UserDtoLogin.builder()
                .email(email)
                .password(password)
                .build();
    }

    UserLoginDtoOutput getPreparedUserLoginDtoOutput() {
        return UserLoginDtoOutput.builder()
                .email(email)
                .token(token)
                .build();
    }

    UserDetails getPreparedUserDetails() {
        Set<GrantedAuthority> authorityList = new HashSet<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + EUserRole.USER.name()));
        return new org.springframework.security.core.userdetails.User(email, password, true, true,
                true, true, authorityList);
    }

    ResponseCookie getPreparedCookie() {
        return ResponseCookie.from(AUTHORIZATION, URLEncoder.encode("Bearer " + token, StandardCharsets.UTF_8))
                .maxAge(JWT_TOKEN_VALID_TIME)
                .httpOnly(true)
                .build();
    }

    UserDtoOutput getPreparedUserDtoOutput() {
        return UserDtoOutput.builder()
                .id(id.toString())
                .email(email)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .dtLogin(dtLogin)
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .build();
    }

    TokenValidationDto getPreparedTokenValidationDto(){
        return TokenValidationDto.builder()
                .id(id.toString())
                .authenticated(true)
                .username(email)
                .role(EUserRole.USER.name())
                .dtUpdate(dtUpdate)
                .build();
    }

    private void checkUserDtoOutputFields(UserDtoOutput actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(email, actual.getEmail());
        assertEquals(EUserRole.USER.name(), actual.getRole());
        assertEquals(username, actual.getUsername());
        assertEquals(EUserStatus.ACTIVATED.name(), actual.getStatus());
        assertEquals(dtLogin, actual.getDtLogin());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
    }
    private void checkTokenValidationOutputFields(TokenValidationDto actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(email, actual.getUsername());
        assertEquals(EUserRole.USER.name(), actual.getRole());
        assertEquals(true, actual.getAuthenticated());
        assertEquals(dtUpdate, actual.getDtUpdate());
    }

}