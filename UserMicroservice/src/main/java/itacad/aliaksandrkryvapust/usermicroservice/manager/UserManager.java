package itacad.aliaksandrkryvapust.usermicroservice.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.usermicroservice.event.EmailVerificationEvent;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.IAuditManager;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.usermicroservice.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IUserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    private final IAuditManager auditManager;
    private final AuditMapper auditMapper;
    private final ApplicationEventPublisher eventPublisher;

    public UserManager(JwtUserDetailsService jwtUserDetailsService, IUserService userService, UserMapper userMapper,
                       JwtTokenUtil jwtTokenUtil, PasswordEncoder encoder, AuditManager auditManager,
                       AuditMapper auditMapper, ApplicationEventPublisher eventPublisher) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtTokenUtil = jwtTokenUtil;
        this.encoder = encoder;
        this.auditManager = auditManager;
        this.auditMapper = auditMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserLoginDtoOutput login(UserDtoLogin userDtoLogin) {
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userDtoLogin.getEmail());
        if (!encoder.matches(userDtoLogin.getPassword(), userDetails.getPassword()) || !userDetails.isEnabled()) {
            throw new BadCredentialsException("User login or password is incorrect or user is not activated");
        }
        String token = jwtTokenUtil.generateToken(userDetails);
        return this.userMapper.loginOutputMapping(userDetails, token);
    }

    @Override
    public UserRegistrationDtoOutput saveUser(UserDtoRegistration userDtoRegistration) {
        try {
            User user = this.userService.save(userMapper.userInputMapping(userDtoRegistration));
            AuditDto auditDto = this.auditMapper.userOutputMapping(user, userPost);
            this.auditManager.audit(auditDto);
            this.eventPublisher.publishEvent(new EmailVerificationEvent(user));
            return userMapper.registerOutputMapping(user);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public UserDtoOutput getUserDto(String email) {
        User user = this.userService.getUser(email);
        return this.userMapper.outputMapping(user);
    }

    @Override
    public UserDtoOutput save(UserDtoInput userDtoInput) {
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
    public UserDtoOutput update(UserDtoInput dtoInput, UUID id, Long version) {
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
