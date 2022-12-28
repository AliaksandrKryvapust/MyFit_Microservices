package itacad.aliaksandrkryvapust.myfitapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IUserService;
import itacad.aliaksandrkryvapust.myfitapp.service.security.JwtUserDetailsService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static itacad.aliaksandrkryvapust.myfitapp.core.Constants.*;

@Component
public class UserManager implements IUserManager {
    private final JwtUserDetailsService jwtUserDetailsService;
    private final IUserService userService;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private final AuditMapper auditMapper;

    public UserManager(JwtUserDetailsService jwtUserDetailsService, IUserService userService, UserMapper userMapper,
                       JwtTokenUtil jwtTokenUtil, PasswordEncoder encoder, ObjectMapper objectMapper, AuditMapper auditMapper) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
        this.auditMapper = auditMapper;
    }

    @Override
    public UserLoginDtoOutput login(UserDtoLogin userDtoLogin) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDtoLogin.getMail());
        if (!encoder.matches(userDtoLogin.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("User login or password is incorrect");
        }
        String token = jwtTokenUtil.generateToken(userDetails);
        return this.userMapper.loginOutputMapping(userDetails, token);
    }

    //    public UserLoginDtoOutput login(UserDtoLogin userDtoLogin) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(userDtoLogin.getUsername(), userDtoLogin.getPassword()));
//        if (authentication.isAuthenticated()) {
//            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDtoLogin.getUsername());
//            String token = jwtTokenUtil.generateToken(userDetails);
//            return this.userMapper.loginOutputMapping(userDetails, token);
//        } else {
//            return null;
//        }
//    }
    @Override
    public UserLoginDtoOutput saveUser(UserDtoRegistration userDtoRegistration, HttpServletRequest request) {
        try {
            User user = this.userService.save(userMapper.userInputMapping(userDtoRegistration));
            audit(request, user);
            return userMapper.registerOutputMapping(user);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    private void audit(HttpServletRequest request, User user) throws JsonProcessingException, URISyntaxException {
        String token = request.getHeader("Authorization");
        AuditDto auditDto = this.auditMapper.userOutputMapping(user);
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(auditDto);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(AUDIT_URI))
                .header(TOKEN_HEADER, token)
                .header(CONTENT, CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpClient.newHttpClient().sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    public UserDtoOutput getUser(String email) {
        User user = this.userService.getUser(email);
        return this.userMapper.outputMapping(user);
    }

    @Override
    public UserDtoOutput save(UserDtoInput userDtoInput) {
        User user = this.userService.save(userMapper.inputMapping(userDtoInput));
        return userMapper.outputMapping(user);
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        return userMapper.outputPageMapping(this.userService.get(pageable));
    }

    @Override
    public UserDtoOutput get(UUID id) {
        User menu = this.userService.get(id);
        return userMapper.outputMapping(menu);
    }

    @Override
    public UserDtoOutput update(UserDtoInput dtoInput, UUID id, Long version) {
        User user = this.userService.update(userMapper.inputMapping(dtoInput), id, version);
        return userMapper.outputMapping(user);
    }
}
