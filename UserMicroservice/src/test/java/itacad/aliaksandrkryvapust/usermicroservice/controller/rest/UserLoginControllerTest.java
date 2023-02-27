package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserRegistrationDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenService;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IUserManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpCookie;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil.JWT_TOKEN_VALID_TIME;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@WebMvcTest(controllers = UserLoginController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
class UserLoginControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IUserManager userManager;
    @MockBean
    private ITokenService tokenService;
    @MockBean
    private JwtUserDetailsService userDetailsService;
    @MockBean
    SecurityContext securityContext;
    // Beans for JwtFilter
    @MockBean
    private JwtTokenUtil tokenUtil;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final String username = "someone";
    final String id = "1d63d7df-f1b3-4e92-95a3-6c7efad96901";
    final String password = "kdrL556D";
    final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBteWZpdC5jb20iLCJpYXQiOjE2NzM1MzE5MzEsImV4cCI6MTY3MzUzNTUzMX0.ncZiUNsJK1LFh2U59moFgWhzcWZyW3p0TL9O_hWVcvw";
    final String multipleError = "structured_error";

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"USER_ADMIN"})
    void getCurrentUser() throws Exception {
        // preconditions
        final UserDtoOutput userDtoOutput = getPreparedUserDtoOutput();
        Mockito.when(userDetailsService.getUserDto(email)).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id));

        //test
        Mockito.verify(userDetailsService).getUserDto(email);
    }

    @Test
    void login() throws Exception {
        // preconditions
        final UserDtoLogin userDtoLogin = getPreparedUserDtoLogin();
        final UserLoginDtoOutput userLoginDtoOutput = getPreparedUserLoginDto();
        final HttpCookie httpCookie = getPreparedCookie();
        Mockito.when(userDetailsService.login(userDtoLogin)).thenReturn(userLoginDtoOutput);
        Mockito.when(userDetailsService.createJwtCookie(token)).thenReturn(httpCookie);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoLogin)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(token));

        //test
        Mockito.verify(userDetailsService).login(userDtoLogin);
    }

    @Test
    void logout() throws Exception {
        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/logout").header(AUTHORIZATION, token))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(userDetailsService, Mockito.times(1)).logout(any(HttpServletRequest.class));
    }

    @Test
    void registration() throws Exception {
        // preconditions
        final UserDtoRegistration userDtoRegistration = getPreparedUserDtoRegistration();
//        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
        final UserRegistrationDtoOutput userRegistration = getPreparedUserRegistration();
        Mockito.when(userManager.saveUser(userDtoRegistration)).thenReturn(userRegistration);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //test
        Mockito.verify(userManager, Mockito.times(1)).saveUser(any(UserDtoRegistration.class));
    }

    @Test
    void registrationConfirmation() throws Exception {
        // preconditions

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/registration/confirm")
                        .param("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(tokenService, Mockito.times(1)).activateUser(token);
    }

    @Test
    void resendToken() throws Exception {
        // preconditions

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/registration/confirm/token")
                        .param("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(tokenService, Mockito.times(1)).resendToken(any(String.class));
    }

    @Test
    void validateUserDtoLoginEmptyEmail() throws Exception {
        // preconditions
        final UserDtoLogin userDtoLogin = UserDtoLogin.builder()
                .email(null)
                .password(password)
                .build();
        final String errorMessage = "username cannot be null";
        final String field = "email";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoLogin)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userDetailsService, Mockito.times(0)).login(userDtoLogin);
    }

    @Test
    void validateUserDtoLoginShortEmail() throws Exception {
        // preconditions
        final UserDtoLogin userDtoLogin = UserDtoLogin.builder()
                .email("a")
                .password(password)
                .build();
        final String errorMessage = "username should contain from 2 to 50 letters";
        final String field = "email";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoLogin)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userDetailsService, Mockito.times(0)).login(userDtoLogin);
    }

    @Test
    void validateUserDtoLoginEmptyPassword() throws Exception {
        // preconditions
        final UserDtoLogin userDtoLogin = UserDtoLogin.builder()
                .email(email)
                .password(null)
                .build();
        final String errorMessage = "password cannot be null";
        final String field = "password";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoLogin)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userDetailsService, Mockito.times(0)).login(userDtoLogin);
    }

    @Test
    void validateUserDtoLoginShortPassword() throws Exception {
        // preconditions
        final UserDtoLogin userDtoLogin = UserDtoLogin.builder()
                .email(email)
                .password("a")
                .build();
        final String errorMessage = "password should contain from 2 to 16 letters";
        final String field = "password";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoLogin)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userDetailsService, Mockito.times(0)).login(userDtoLogin);
    }

    @Test
    void validateUserDtoRegistrationEmptyUsername() throws Exception {
        // preconditions
        final UserDtoRegistration userDtoRegistration = UserDtoRegistration.builder()
                .email(email)
                .password(password)
                .username(null)
                .build();
        final String errorMessage = "username cannot be null";
        final String field = "username";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveUser(userDtoRegistration);
    }

    @Test
    void validateUserDtoRegistrationShortUsername() throws Exception {
        // preconditions
        final UserDtoRegistration userDtoRegistration = UserDtoRegistration.builder()
                .email(email)
                .password(password)
                .username("a")
                .build();
        final String errorMessage = "username should contain from 2 to 50 letters";
        final String field = "username";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveUser(userDtoRegistration);
    }

    @Test
    void validateUserDtoRegistrationEmptyPassword() throws Exception {
        // preconditions
        final UserDtoRegistration userDtoRegistration = UserDtoRegistration.builder()
                .email(email)
                .password(null)
                .username(username)
                .build();
        final String errorMessage = "password cannot be null";
        final String field = "password";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveUser(userDtoRegistration);
    }

    @Test
    void validateUserDtoRegistrationShortPassword() throws Exception {
        // preconditions
        final UserDtoRegistration userDtoRegistration = UserDtoRegistration.builder()
                .email(email)
                .password("a")
                .username(username)
                .build();
        final String errorMessage = "password should contain from 2 to 200 letters";
        final String field = "password";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveUser(userDtoRegistration);
    }

    @Test
    void validateUserDtoRegistrationEmptyEmail() throws Exception {
        // preconditions
        final UserDtoRegistration userDtoRegistration = UserDtoRegistration.builder()
                .email(null)
                .password(password)
                .username(username)
                .build();
        final String errorMessage = "email is not valid";
        final String field = "email";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveUser(userDtoRegistration);
    }

    @Test
    void validateUserDtoRegistrationIncorrectEmail() throws Exception {
        // preconditions
        final UserDtoRegistration userDtoRegistration = UserDtoRegistration.builder()
                .email("abs")
                .password(password)
                .username(username)
                .build();
        final String errorMessage = "email is not valid";
        final String field = "email";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.logref").value(multipleError))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].field").value(field))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(userManager, Mockito.times(0)).saveUser(userDtoRegistration);
    }

    private UserLoginDtoOutput getPreparedUserLoginDto(){
        return UserLoginDtoOutput.builder()
                .email(email)
                .token(token)
                .build();
    }

    private UserRegistrationDtoOutput getPreparedUserRegistration() {
        return UserRegistrationDtoOutput.builder()
                .email(email)
                .role(EUserRole.USER.name())
                .build();
    }

    private UserDtoOutput getPreparedUserDtoOutput() {
        return UserDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .email(email)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .id(id)
                .build();
    }

    private UserDtoLogin getPreparedUserDtoLogin() {
        return UserDtoLogin.builder()
                .email(email)
                .password(password)
                .build();
    }

    private UserDtoRegistration getPreparedUserDtoRegistration(){
        return UserDtoRegistration.builder()
                .email(email)
                .password(password)
                .username(username).build();
    }

    private HttpCookie getPreparedCookie() {
        return ResponseCookie.from(AUTHORIZATION, URLEncoder.encode("Bearer " + token, StandardCharsets.UTF_8))
                .maxAge(JWT_TOKEN_VALID_TIME)
                .httpOnly(true)
                .build();
    }
}