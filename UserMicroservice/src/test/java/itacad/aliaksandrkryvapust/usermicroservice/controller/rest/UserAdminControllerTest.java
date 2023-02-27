package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IUserManager;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;

@WebMvcTest(controllers = UserAdminController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
class UserAdminControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IUserManager userManager;
    @MockBean
    SecurityContext securityContext;
    // Beans for JwtFilter
    @MockBean
    private JwtTokenUtil tokenUtil;
    @MockBean
    private JwtUserDetailsService userDetailsService;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    final LocalDate dtLogin = LocalDate.parse("04/04/2023", df);
    final String dt_login = "04/04/2023";
    final String email = "admin@myfit.com";
    final String username = "someone";
    final String id = "1d63d7df-f1b3-4e92-95a3-6c7efad96901";
    final String password = "kdrL556D";
    final String multipleError = "structured_error";

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void getPage() throws Exception {
        // preconditions
        final Pageable pageable = PageRequest.of(0, 1, Sort.by("dtCreate").descending());
        final PageDtoOutput<UserDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        Mockito.when(userManager.getDto(pageable)).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/users").param("page", "0")
                        .param("size", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_login").value(dt_login))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id").value(id));

        //test
        Mockito.verify(userManager).getDto(pageable);
    }

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void get() throws Exception {
        // preconditions
        final UserDtoOutput userDtoOutput = getPreparedUserDtoOutput();
        Mockito.when(userManager.getDto(UUID.fromString(id))).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin/users/1d63d7df-f1b3-4e92-95a3-6c7efad96901"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_login").value(dt_login))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));

        //test
        Mockito.verify(userManager).getDto(UUID.fromString(id));
    }

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void post() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = getPreparedUserDtoInput();
        final UserDtoOutput userDtoOutput = getPreparedUserDtoOutput();
        Mockito.when(userManager.saveDto(userDtoInput)).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_login").value(dt_login))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));

        //test
        Mockito.verify(userManager).saveDto(userDtoInput);
    }

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void put() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = getPreparedUserDtoInput();
        final UserDtoOutput userDtoOutput = getPreparedUserDtoOutput();
        Mockito.when(userManager.updateDto(userDtoInput, UUID.fromString(id), dtUpdate.toEpochMilli())).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/admin/users/1d63d7df-f1b3-4e92-95a3-6c7efad96901/version/1673532532870")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_login").value(dt_login))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));

        //test
        Mockito.verify(userManager).updateDto(userDtoInput, UUID.fromString(id), dtUpdate.toEpochMilli());
    }

    @Test
    void validateUserDtoInputEmptyUsername() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(null)
                .email(email)
                .role(EUserRole.ADMIN.name())
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final String errorMessage = "username cannot be null";
        final String field = "username";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    @Test
    void validateUserDtoInputShortUsername() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username("u")
                .email(email)
                .role(EUserRole.ADMIN.name())
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final String errorMessage = "username should contain from 2 to 50 letters";
        final String field = "username";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    @Test
    void validateUserDtoInputEmptyPassword() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(username)
                .email(email)
                .role(EUserRole.ADMIN.name())
                .password(null)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final String errorMessage = "password cannot be null";
        final String field = "password";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    @Test
    void validateUserDtoInputShortPassword() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(username)
                .email(email)
                .role(EUserRole.ADMIN.name())
                .password("1")
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final String errorMessage = "password should contain from 2 to 200 letters";
        final String field = "password";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    @Test
    void validateUserDtoInputEmptyEmail() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(username)
                .email(null)
                .role(EUserRole.ADMIN.name())
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final String errorMessage = "email is not valid";
        final String field = "email";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    @Test
    void validateUserDtoInputIncorrectEmail() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(username)
                .email("email")
                .role(EUserRole.ADMIN.name())
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final String errorMessage = "email is not valid";
        final String field = "email";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    @Test
    void validateUserDtoInputEmptyRole() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(username)
                .email(email)
                .role(null)
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final String errorMessage = "user role cannot be null";
        final String field = "role";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }


    @Test
    void validateUserDtoInputIncorrectRole() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(username)
                .email(email)
                .role("role")
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final String errorMessage = "user role does not match";
        final String field = "role";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    @Test
    void validateUserDtoInputEmptyStatus() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(username)
                .email(email)
                .role(EUserRole.USER.name())
                .password(password)
                .status(null)
                .build();
        final String errorMessage = "user status cannot be null";
        final String field = "status";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    @Test
    void validateUserDtoInputIncorrectStatus() throws Exception {
        // preconditions
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .username(username)
                .email(email)
                .role(EUserRole.USER.name())
                .password(password)
                .status("active")
                .build();
        final String errorMessage = "user status does not match";
        final String field = "status";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveDto(userDtoInput);
    }

    private UserDtoInput getPreparedUserDtoInput() {
        return UserDtoInput.builder()
                .username(username)
                .email(email)
                .role(EUserRole.USER.name())
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
    }

    private UserDtoOutput getPreparedUserDtoOutput() {
        return UserDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .dtLogin(dtLogin)
                .email(email)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .id(id)
                .build();
    }

    PageDtoOutput<UserDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<UserDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedUserDtoOutput()))
                .build();
    }
}