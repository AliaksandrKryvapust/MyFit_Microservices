package itacad.aliaksandrkryvapust.usermicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.AuditMapper;
import itacad.aliaksandrkryvapust.usermicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.usermicroservice.event.EmailVerificationEvent;
import itacad.aliaksandrkryvapust.usermicroservice.repository.api.IUserRepository;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IUserManager;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IUserService;
import itacad.aliaksandrkryvapust.usermicroservice.service.validator.api.IUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService, IUserManager {
    private final static String userPost = "New user was created";
    private final static String userPut = "User was updated";
    private final IUserRepository userRepository;
    private final IUserValidator userValidator;
    private final IAuditManager auditManager;
    private final UserMapper userMapper;
    private final AuditMapper auditMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Page<User> get(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User get(UUID id) {
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User update(User user, UUID id, Long version) {
        User currentEntity = get(id);
        userValidator.optimisticLockCheck(version, currentEntity);
        userMapper.updateEntityFields(user, currentEntity);
        return save(currentEntity);
    }

    @Override
    public UserRegistrationDtoOutput saveUser(UserDtoRegistration userDtoRegistration) {
        try {
            User entityToSave = userMapper.userInputMapping(userDtoRegistration);
            userValidator.validateEntity(entityToSave);
            User user = save(entityToSave);
            AuditDto auditDto = auditMapper.userOutputMapping(user, userPost);
            auditManager.audit(auditDto);
            eventPublisher.publishEvent(new EmailVerificationEvent(user));
            return userMapper.registerOutputMapping(user);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public UserDtoOutput saveDto(UserDtoInput userDtoInput) {
        try {
            User entityToSave = userMapper.inputMapping(userDtoInput);
            userValidator.validateEntity(entityToSave);
            User user = save(entityToSave);
            AuditDto auditDto = auditMapper.userOutputMapping(user, userPost);
            auditManager.audit(auditDto);
            return userMapper.outputMapping(user);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public PageDtoOutput<UserDtoOutput> getDto(Pageable pageable) {
        Page<User> page = get(pageable);
        return userMapper.outputPageMapping(page);
    }

    @Override
    public UserDtoOutput getDto(UUID id) {
        User user = get(id);
        return userMapper.outputMapping(user);
    }

    @Override
    public UserDtoOutput updateDto(UserDtoInput dtoInput, UUID id, Long version) {
        try {
            User entityToSave = userMapper.inputMapping(dtoInput);
            userValidator.validateEntity(entityToSave);
            User user = update(entityToSave, id, version);
            AuditDto auditDto = auditMapper.userOutputMapping(user, userPut);
            auditManager.audit(auditDto);
            return userMapper.outputMapping(user);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }
}
