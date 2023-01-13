package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoRegistration;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserLoginDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc
class UserControllerTest {
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
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void getPage() throws Exception {
        // preconditions
        final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
        final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
        final String email = "admin@myfit.com";
        final String username = "someone";
        final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
        final Pageable pageable = Pageable.ofSize(5).first();
        final UserDtoOutput userDtoOutput = UserDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .mail(email)
                .nick(username)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVATED)
                .uuid(id)
                .build();
        final PageDtoOutput pageDtoOutput = PageDtoOutput.builder()
                .number(1)
                .size(10)
                .totalPages(2)
                .totalElements(10L)
                .first(true)
                .last(false)
                .numberOfElements(2)
                .content(Collections.singletonList(userDtoOutput))
                .build();
        Mockito.when(userManager.get(pageable)).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users").param("page", "0")
                        .param("size", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].mail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].nick").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].role").value(UserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].status").value(UserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].uuid").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number_of_elements").value(2));

        //test
        Mockito.verify(userManager).get(pageable);
    }

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void get() throws Exception {
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
        Mockito.when(userManager.get(id)).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1d63d7df-f1b3-4e92-95a3-6c7efad96901"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nick").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(UserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(UserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(id.toString()));

        //test
        Mockito.verify(userManager).get(id);
    }

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void post() throws Exception {
        // preconditions
        final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
        final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
        final String email = "admin@myfit.com";
        final String username = "someone";
        final String password = "kdrL556D";
        final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .nick(username)
                .mail(email)
                .role(UserRole.USER.name())
                .password(password)
                .status(UserStatus.ACTIVATED.name())
                .build();
        final UserDtoOutput userDtoOutput = UserDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .mail(email)
                .nick(username)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVATED)
                .uuid(id)
                .build();
        Mockito.when(userManager.save(userDtoInput)).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //test
        Mockito.verify(userManager).save(userDtoInput);
    }

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void put() throws Exception {
        // preconditions
        final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
        final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
        final String email = "admin@myfit.com";
        final String username = "someone";
        final String password = "kdrL556D";
        final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
        final UserDtoInput userDtoInput = UserDtoInput.builder()
                .nick(username)
                .mail(email)
                .role(UserRole.USER.name())
                .password(password)
                .status(UserStatus.ACTIVATED.name())
                .build();
        final UserDtoOutput userDtoOutput = UserDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .mail(email)
                .nick(username)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVATED)
                .uuid(id)
                .build();
        Mockito.when(userManager.save(userDtoInput)).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/1d63d7df-f1b3-4e92-95a3-6c7efad96901/dt_update/1673532532870")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //test
        Mockito.verify(userManager).save(userDtoInput);
    }
}