package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.usermicroservice.controller.filter.JwtFilter;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.ITokenManager;
import itacad.aliaksandrkryvapust.usermicroservice.manager.api.IUserManager;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static itacad.aliaksandrkryvapust.usermicroservice.core.Constants.TOKEN_HEADER;

@WebMvcTest(controllers = UserLoginController.class)
@AutoConfigureMockMvc
class UserLoginControllerTest {
    Logger logger = LoggerFactory.getLogger(UserLoginController.class);
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
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
    void login() {
    }

    @Test
    void registration() {
    }

    @Test
    void registrationConfirmation() {
    }

    @Test
    void resendToken() {
    }
}