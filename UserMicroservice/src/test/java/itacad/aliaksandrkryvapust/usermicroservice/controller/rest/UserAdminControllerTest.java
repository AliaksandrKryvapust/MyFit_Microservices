package itacad.aliaksandrkryvapust.usermicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.usermicroservice.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.input.UserDtoInput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.ITokenManager;
import itacad.aliaksandrkryvapust.usermicroservice.service.api.IUserManager;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.EUserStatus;
import itacad.aliaksandrkryvapust.usermicroservice.service.JwtUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.time.Instant;
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
                .email(email)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .id(id.toString())
                .build();
        final PageDtoOutput<UserDtoOutput> pageDtoOutput = PageDtoOutput.<UserDtoOutput>builder()
                .number(1)
                .size(10)
                .totalPages(2)
                .totalElements(10L)
                .first(true)
                .last(false)
                .numberOfElements(2)
                .content(Collections.singletonList(userDtoOutput))
                .build();
        Mockito.when(userManager.getDto(pageable)).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users").param("page", "0")
                        .param("size", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].mail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].nick").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].uuid").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number_of_elements").value(2));

        //test
        Mockito.verify(userManager).getDto(pageable);
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
                .email(email)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .id(id.toString())
                .build();
        Mockito.when(userManager.getDto(id)).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1d63d7df-f1b3-4e92-95a3-6c7efad96901"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nick").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(id.toString()));

        //test
        Mockito.verify(userManager).getDto(id);
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
                .username(username)
                .email(email)
                .role(EUserRole.USER.name())
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final UserDtoOutput userDtoOutput = UserDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .email(email)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .id(id.toString())
                .build();
        Mockito.when(userManager.saveDto(userDtoInput)).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nick").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(id.toString()));

        //test
        Mockito.verify(userManager).saveDto(userDtoInput);
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
                .username(username)
                .email(email)
                .role(EUserRole.USER.name())
                .password(password)
                .status(EUserStatus.ACTIVATED.name())
                .build();
        final UserDtoOutput userDtoOutput = UserDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .email(email)
                .username(username)
                .role(EUserRole.USER.name())
                .status(EUserStatus.ACTIVATED.name())
                .id(id.toString())
                .build();
        Mockito.when(userManager.updateDto(userDtoInput, id, dtUpdate.toEpochMilli())).thenReturn(userDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/1d63d7df-f1b3-4e92-95a3-6c7efad96901/dt_update/1673532532870")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mail").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nick").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(EUserRole.USER.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(EUserStatus.ACTIVATED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(id.toString()));

        //test
        Mockito.verify(userManager).updateDto(userDtoInput, id, dtUpdate.toEpochMilli());
    }
}