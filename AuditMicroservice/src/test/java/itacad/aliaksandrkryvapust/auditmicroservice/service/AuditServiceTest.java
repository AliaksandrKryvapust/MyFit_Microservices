package itacad.aliaksandrkryvapust.auditmicroservice.service;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.UserDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.mapper.AuditMapper;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.api.IAuditRepository;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.Audit;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EType;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.User;
import itacad.aliaksandrkryvapust.auditmicroservice.service.validator.api.IAuditValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {
    @InjectMocks
    private AuditService auditService;
    @Mock
    private IAuditRepository auditRepository;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private IAuditValidator auditValidator;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final String text = "product was added";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final EUserRole role = EUserRole.USER;
    final EType type = EType.PRODUCT;
    final String email = "admin@myfit.com";

    @Test
    void save() {
        // preconditions
        final Audit auditInput = getPreparedAuditInput();
        ArgumentCaptor<Audit> actualAudit = ArgumentCaptor.forClass(Audit.class);

        //test
        auditService.save(auditInput);
        Mockito.verify(auditRepository, Mockito.times(1)).insert(actualAudit.capture());

        // assert
        assertEquals(auditInput, actualAudit.getValue());
    }

    @Test
    void get() {
        // preconditions
        final Audit auditOutput = getPreparedAuditOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Audit> page = new PageImpl<>(Collections.singletonList(auditOutput), pageable, 1);
        Mockito.when(auditRepository.findAll(pageable)).thenReturn(page);

        //test
        Page<Audit> actual = auditService.get(pageable);

        // assert
        assertNotNull(actual);
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.isFirst());
        for (Audit audit : actual.getContent()) {
            checkAuditOutputFields(audit);
        }
    }

    @Test
    void getAction() {
        // preconditions
        final Audit auditOutput = getPreparedAuditOutput();
        Mockito.when(auditRepository.findByActionId(id)).thenReturn(Collections.singletonList(auditOutput));

        //test
        List<Audit> actual = auditService.getAction(id);

        // assert
        assertNotNull(actual);
        actual.forEach(this::checkAuditOutputFields);
    }

    @Test
    void saveDto() {
        // preconditions
        final AuditDto dtoInput = getPreparedAuditDtoInput();
        final Audit auditInput = getPreparedAuditInput();
        Mockito.when(auditMapper.inputMapping(dtoInput)).thenReturn(auditInput);
        ArgumentCaptor<Audit> actualAudit = ArgumentCaptor.forClass(Audit.class);

        //test
        auditService.saveDto(dtoInput);
        Mockito.verify(auditValidator, Mockito.times(1)).validateEntity(actualAudit.capture());
        Mockito.verify(auditRepository, Mockito.times(1)).insert(actualAudit.capture());

        // assert
        assertEquals(auditInput, actualAudit.getValue());
    }

    @Test
    void getDto() {
        // preconditions
        final Audit auditOutput = getPreparedAuditOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Audit> page = new PageImpl<>(Collections.singletonList(auditOutput), pageable, 1);
        final PageDtoOutput<AuditDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        Mockito.when(auditRepository.findAll(pageable)).thenReturn(page);
        Mockito.when(auditMapper.outputPageMapping(page)).thenReturn(pageDtoOutput);

        //test
        PageDtoOutput<AuditDtoOutput> actual = auditService.getDto(pageable);

        // assert
        assertNotNull(actual);
        checkPageDtoOutputFields(actual);
        actual.getContent().forEach(this::checkAuditDtoOutputFields);
    }

    @Test
    void getActionDto() {
        // preconditions
        final Audit auditOutput = getPreparedAuditOutput();
        final AuditDtoOutput auditDtoOutput = getPreparedAuditDtoOutput();
        Mockito.when(auditRepository.findByActionId(id)).thenReturn(Collections.singletonList(auditOutput));
        Mockito.when(auditMapper.outputListMapping(Collections.singletonList(auditOutput)))
                .thenReturn(Collections.singletonList(auditDtoOutput));

        //test
        List<AuditDtoOutput> actual = auditService.getActionDto(id);

        // assert
        assertNotNull(actual);
        actual.forEach(this::checkAuditDtoOutputFields);
    }

    private User getPreparedUser() {
        return User.builder()
                .role(role)
                .email(email)
                .id(id)
                .build();
    }

    private Audit getPreparedAuditInput() {
        return Audit.builder()
                .type(type)
                .text(text)
                .user(getPreparedUser())
                .actionId(id)
                .build();
    }

    private Audit getPreparedAuditOutput() {
        Audit audit = getPreparedAuditInput();
        audit.setId(id);
        audit.setDtCreate(dtCreate);
        return audit;
    }

    private UserDto getPreparedUserDtoInput() {
        return UserDto.builder()
                .role(role.name())
                .email(email)
                .id(id.toString())
                .build();
    }

    private AuditDto getPreparedAuditDtoInput() {
        return AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(getPreparedUserDtoInput())
                .id(id.toString())
                .build();
    }

    private UserDtoOutput getPreparedUserDtoOutput() {
        return UserDtoOutput.builder()
                .role(role.name())
                .email(email)
                .id(id.toString())
                .build();
    }

    private AuditDtoOutput getPreparedAuditDtoOutput() {
        UserDtoOutput user = getPreparedUserDtoOutput();
        return AuditDtoOutput.builder()
                .dtCreate(dtCreate)
                .type(type.name())
                .text(text)
                .user(user)
                .actionId(id.toString())
                .id(id.toString())
                .build();
    }

    PageDtoOutput<AuditDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<AuditDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedAuditDtoOutput()))
                .build();
    }

    private void checkAuditOutputFields(Audit actual) {
        assertEquals(id, actual.getId());
        assertEquals(id, actual.getActionId());
        assertEquals(text, actual.getText());
        assertEquals(type, actual.getType());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(id, actual.getUser().getId());
        assertEquals(email, actual.getUser().getEmail());
        assertEquals(role, actual.getUser().getRole());
    }

    private void checkAuditDtoOutputFields(AuditDtoOutput actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(id.toString(), actual.getActionId());
        assertEquals(text, actual.getText());
        assertEquals(type.name(), actual.getType());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(id.toString(), actual.getUser().getId());
        assertEquals(email, actual.getUser().getEmail());
        assertEquals(role.name(), actual.getUser().getRole());
    }

    private void checkPageDtoOutputFields(PageDtoOutput<AuditDtoOutput> actual) {
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.getFirst());
        Assertions.assertTrue(actual.getLast());
        assertEquals(2, actual.getNumber());
        assertEquals(1, actual.getNumberOfElements());
        assertEquals(1, actual.getSize());
        assertEquals(1, actual.getTotalPages());
        assertEquals(1, actual.getTotalElements());
    }
}