package itacad.aliaksandrkryvapust.myfitapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import itacad.aliaksandrkryvapust.myfitapp.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IUserService;
import itacad.aliaksandrkryvapust.myfitapp.service.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class UserManager implements IUserManager {
    private final static String userPost = "New user was created";
    private final static String userPut = "User was updated";
    private final JwtUserDetailsService jwtUserDetailsService;
    private final IUserService userService;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder encoder;
    private final AuditManager auditManager;

    private final AuditMapper auditMapper;

    @Autowired
    public UserManager(JwtUserDetailsService jwtUserDetailsService, IUserService userService, UserMapper userMapper,
                       JwtTokenUtil jwtTokenUtil, PasswordEncoder encoder, AuditManager auditManager, AuditMapper auditMapper) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.encoder = encoder;
        this.auditManager = auditManager;
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

    @Override
    public UserLoginDtoOutput saveUser(UserDtoRegistration userDtoRegistration, HttpServletRequest request) {
        try {
            User user = this.userService.save(userMapper.userInputMapping(userDtoRegistration));
            AuditDto auditDto = this.auditMapper.userOutputMapping(user, userPost);
            this.auditManager.audit(auditDto);
            return userMapper.registerOutputMapping(user);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }



    @Override
    public UserDtoOutput getUser(String email) {
        User user = this.userService.getUser(email);
        return this.userMapper.outputMapping(user);
    }

    @Override
    public UserDtoOutput save(UserDtoInput userDtoInput, HttpServletRequest request) {
        try {
            User user = this.userService.save(userMapper.inputMapping(userDtoInput));
            AuditDto auditDto = this.auditMapper.userOutputMapping(user, userPost);
            this.auditManager.audit(auditDto);
            return userMapper.outputMapping(user);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
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
    public UserDtoOutput update(UserDtoInput dtoInput, UUID id, Long version, HttpServletRequest request) {
        try {
            User user = this.userService.update(userMapper.inputMapping(dtoInput), id, version);
            AuditDto auditDto = this.auditMapper.userOutputMapping(user, userPut);
            this.auditManager.audit(auditDto);
            return userMapper.outputMapping(user);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }
}
