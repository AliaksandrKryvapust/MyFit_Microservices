package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoLogin;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

@WebMvcTest(controllers = UserLoginController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
class UserLoginControllerTest {
    Logger logger = LoggerFactory.getLogger(UserLoginController.class);
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
    private ITokenManager tokenManager;
    @MockBean
    private JwtUserDetailsService userDetailsService;

    @Test
    @WithMockUser(username="admin@myfit.com", password = "kdrL556D",roles={"USER_ADMIN"})
    void getCurrentUser() throws Exception {
        // preconditions
        final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
        final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
        final String email = "admin@myfit.com";
        final String username = "someone";
        final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
        final UserDtoOutput userDtoOutput = UserDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .mail(email)
                .nick(username)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVATED)
                .uuid(id)
                .build();
        Mockito.when(userManager.getUser(email)).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nick").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(UserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(UserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(id.toString()));

        //test
        Mockito.verify(userManager).getUser(email);
    }

    @Test
    void login() throws Exception {
        // preconditions
        final String email = "admin@myfit.com";
        final String password = "kdrL556D";
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBteWZpdC5jb20iLCJpYXQiOjE2NzM1MzE5MzEsImV4cCI6MTY3MzUzNTUzMX0.ncZiUNsJK1LFh2U59moFgWhzcWZyW3p0TL9O_hWVcvw";
        final UserDtoLogin userDtoLogin = UserDtoLogin.builder()
                .mail(email)
                .password(password).build();
        final UserLoginDtoOutput userLoginDtoOutput = UserLoginDtoOutput.builder()
                .mail(email)
                .token(token).build();
        Mockito.when(userManager.login(userDtoLogin)).thenReturn(userLoginDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoLogin)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(token));

        //test
        Mockito.verify(userManager).login(userDtoLogin);
    }

    @Test
    void registration() throws Exception {
        // preconditions
        final String username = "someone";
        final String email = "admin@myfit.com";
        final String password = "kdrL556D";
        final UserDtoRegistration userDtoRegistration = UserDtoRegistration.builder()
                .mail(email)
                .password(password)
                .nick(username).build();
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
        final UserLoginDtoOutput userLoginDtoOutput = UserLoginDtoOutput.builder()
                .mail(email)
                .build();
        Mockito.when(userManager.saveUser(userDtoRegistration, request)).thenReturn(userLoginDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //test
        Mockito.verify(userManager, Mockito.times(1)).saveUser(any(UserDtoRegistration.class),
                any(HttpServletRequest.class));
    }

    @Test
    void registrationConfirmation() throws Exception {
        // preconditions
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBteWZpdC5jb20iLCJpYXQiOjE2NzM1MzE5MzEsImV4cCI6MTY3MzUzNTUzMX0.ncZiUNsJK1LFh2U59moFgWhzcWZyW3p0TL9O_hWVcvw";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/registration/confirm")
                        .param("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(tokenManager, Mockito.times(1)).validateToken(token);
    }

    @Test
    void resendToken() throws Exception {
        // preconditions
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBteWZpdC5jb20iLCJpYXQiOjE2NzM1MzE5MzEsImV4cCI6MTY3MzUzNTUzMX0.ncZiUNsJK1LFh2U59moFgWhzcWZyW3p0TL9O_hWVcvw";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/registration/confirm/token")
                        .param("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(tokenManager, Mockito.times(1)).resendToken(any(String.class),
                any(HttpServletRequest.class));
    }
}