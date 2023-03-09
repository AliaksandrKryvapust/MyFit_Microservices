package itacad.aliaksandrkryvapust.auditmicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.UserDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.auditmicroservice.core.mapper.UserMapper;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EType;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.auditmicroservice.service.api.IAuditManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuditController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class AuditControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IAuditManager auditManager;
    @MockBean
    SecurityContext securityContext;
    // Beans for JwtFilter
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private TokenMapper tokenMapper;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final String text = "product was added";
    final String id = "1d63d7df-f1b3-4e92-95a3-6c7efad96901";
    final EUserRole role = EUserRole.USER;
    final EType type = EType.PRODUCT;
    final String email = "admin@myfit.com";
    final String multipleError = "structured_error";


    @Test
    void getPage() throws Exception {
        // preconditions
        final Pageable pageable = PageRequest.of(0, 1, Sort.by("dtCreate").descending());
        final PageDtoOutput<AuditDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        Mockito.when(auditManager.getDto(pageable)).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/audit").param("page", "0")
                        .param("size", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content[*].dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].type").value(type.name()))
                .andExpect(jsonPath("$.content[*].text").value(text))
                .andExpect(jsonPath("$.content[*].action_id").value(id))
                .andExpect(jsonPath("$.content[*].id").value(id))
                .andExpect(jsonPath("$.content[*].user.role").value(role.name()))
                .andExpect(jsonPath("$.content[*].user.email").value(email))
                .andExpect(jsonPath("$.content[*].user.id").value(id));

        //test
        Mockito.verify(auditManager).getDto(pageable);
    }

    @Test
    void get() throws Exception {
        // preconditions
        final AuditDtoOutput auditDtoOutput = getPreparedAuditDtoOutput();
        Mockito.when(auditManager.getActionDto(UUID.fromString(id))).thenReturn(Collections.singletonList(auditDtoOutput));

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/audit/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.[*].type").value(type.name()))
                .andExpect(jsonPath("$.[*].text").value(text))
                .andExpect(jsonPath("$.[*].action_id").value(id))
                .andExpect(jsonPath("$.[*].id").value(id))
                .andExpect(jsonPath("$.[*].user.role").value(role.name()))
                .andExpect(jsonPath("$.[*].user.email").value(email))
                .andExpect(jsonPath("$.[*].user.id").value(id));

        //test
        Mockito.verify(auditManager).getActionDto(UUID.fromString(id));
    }

    @Test
    void post() throws Exception {
        // preconditions
        final AuditDto auditDto = getPreparedAuditDtoInput();

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //test
        Mockito.verify(auditManager, Mockito.times(1)).saveDto(auditDto);
    }

    @Test
    void validateAuditDtoInputEmptyActionId() throws Exception {
        // preconditions
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(getPreparedUserDtoInput())
                .id(null)
                .build();
        final String errorMessage = "action id cannot be null";
        final String field = "id";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateAuditDtoInputShortActionId() throws Exception {
        // preconditions
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(getPreparedUserDtoInput())
                .id("a")
                .build();
        final String errorMessage = "action id should contain from 2 to 100 letters";
        final String field = "id";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateAuditDtoInputEmptyUser() throws Exception {
        // preconditions
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(null)
                .id(id)
                .build();
        final String errorMessage = "user cannot be null";
        final String field = "user";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateAuditDtoInputEmptyText() throws Exception {
        // preconditions
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(null)
                .user(getPreparedUserDtoInput())
                .id(id)
                .build();
        final String errorMessage = "text cannot be null";
        final String field = "text";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateAuditDtoInputShortText() throws Exception {
        // preconditions
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text("a")
                .user(getPreparedUserDtoInput())
                .id(id)
                .build();
        final String errorMessage = "text should contain from 2 to 100 letters";
        final String field = "text";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateAuditDtoInputEmptyType() throws Exception {
        // preconditions
        final AuditDto auditDto = AuditDto.builder()
                .type(null)
                .text(text)
                .user(getPreparedUserDtoInput())
                .id(id)
                .build();
        final String errorMessage = "type cannot be null";
        final String field = "type";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateAuditDtoInputWrongType() throws Exception {
        // preconditions
        final AuditDto auditDto = AuditDto.builder()
                .type("type")
                .text(text)
                .user(getPreparedUserDtoInput())
                .id(id)
                .build();
        final String errorMessage = "type does not match";
        final String field = "type";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateUserDtoInputEmptyId() throws Exception {
        // preconditions
        final UserDto userDto = UserDto.builder()
                .role(role.name())
                .email(email)
                .id(null)
                .build();
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(userDto)
                .id(id)
                .build();
        final String errorMessage = "id cannot be blank";
        final String field = "user.id";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateUserDtoInputBlankId() throws Exception {
        // preconditions
        final UserDto userDto = UserDto.builder()
                .role(role.name())
                .email(email)
                .id("   ")
                .build();
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(userDto)
                .id(id)
                .build();
        final String errorMessage = "id cannot be blank";
        final String field = "user.id";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateUserDtoInputEmptyEmail() throws Exception {
        // preconditions
        final UserDto userDto = UserDto.builder()
                .role(role.name())
                .email(null)
                .id(id)
                .build();
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(userDto)
                .id(id)
                .build();
        final String errorMessage = "email cannot be null";
        final String field = "user.email";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateUserDtoInputShortEmail() throws Exception {
        // preconditions
        final UserDto userDto = UserDto.builder()
                .role(role.name())
                .email("a")
                .id(id)
                .build();
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(userDto)
                .id(id)
                .build();
        final String errorMessage = "email should contain from 2 to 100 letters";
        final String field = "user.email";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateUserDtoInputEmptyRole() throws Exception {
        // preconditions
        final UserDto userDto = UserDto.builder()
                .role(null)
                .email(email)
                .id(id)
                .build();
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(userDto)
                .id(id)
                .build();
        final String errorMessage = "user role cannot be null";
        final String field = "user.role";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    @Test
    void validateUserDtoInputWrongRole() throws Exception {
        // preconditions
        final UserDto userDto = UserDto.builder()
                .role("role")
                .email(email)
                .id(id)
                .build();
        final AuditDto auditDto = AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(userDto)
                .id(id)
                .build();
        final String errorMessage = "user role does not match";
        final String field = "user.role";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/audit/").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(auditManager, Mockito.times(0)).saveDto(auditDto);
    }

    private UserDto getPreparedUserDtoInput() {
        return UserDto.builder()
                .role(role.name())
                .email(email)
                .id(id)
                .build();
    }

    private AuditDto getPreparedAuditDtoInput() {
        return AuditDto.builder()
                .type(type.name())
                .text(text)
                .user(getPreparedUserDtoInput())
                .id(id)
                .build();
    }

    private UserDtoOutput getPreparedUserDtoOutput() {
        return UserDtoOutput.builder()
                .role(role.name())
                .email(email)
                .id(id)
                .build();
    }

    private AuditDtoOutput getPreparedAuditDtoOutput() {
        UserDtoOutput user = getPreparedUserDtoOutput();
        return AuditDtoOutput.builder()
                .dtCreate(dtCreate)
                .type(type.name())
                .text(text)
                .user(user)
                .actionId(id)
                .id(id)
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
}