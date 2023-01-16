package aliaksandrkryvapust.reportmicroservice.controller.rest;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.ParamsDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.mapper.microservices.UserMapper;
import aliaksandrkryvapust.reportmicroservice.manager.api.IReportManager;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;


@WebMvcTest(controllers = ReportController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IReportManager reportManager;
    // Beans for JwtFilter
    @MockBean
    private UserMapper userMapper;

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void getPage() throws Exception {
        // preconditions
        final LocalDate from = LocalDate.parse("2022-12-21");
        final LocalDate to = LocalDate.parse("2023-01-12");
        final Long dtCreate = 1673532204657L;
        final Long dtUpdate = 1673532532870L;
        final String description = "description";
        final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
        final Pageable pageable = Pageable.ofSize(5).first();
        final ParamsDtoOutput paramsDtoOutput = ParamsDtoOutput.builder()
                .from(from)
                .to(to)
                .build();
        final ReportDtoOutput reportDtoOutput = ReportDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .uuid(id)
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description(description)
                .params(paramsDtoOutput)
                .build();
        final PageDtoOutput pageDtoOutput = PageDtoOutput.builder()
                .number(1)
                .size(10)
                .totalPages(2)
                .totalElements(10L)
                .first(true)
                .last(false)
                .numberOfElements(2)
                .content(Collections.singletonList(reportDtoOutput))
                .build();
        Mockito.when(reportManager.get(pageable)).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/report").param("page", "0")
                        .param("size", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_create").value(dtCreate))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_update").value(dtUpdate))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].uuid").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].status").value(EStatus.LOADED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].type").value(EType.JOURNAL_FOOD.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].description").value(description))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].params.from").value(from.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].params.to").value(to.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number_of_elements").value(2));

        //test
        Mockito.verify(reportManager).get(pageable);
    }

    @Test
    void export() {
//        // preconditions
//        final String username = "someone";
//        final String email = "admin@myfit.com";
//        final String password = "kdrL556D";
//        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
//        final InputStream inputStream = Mockito.mock(InputStream.class);
//        Mockito.when(userManager.saveUser(userDtoRegistration, request)).thenReturn(userLoginDtoOutput);
//
//        // assert
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration").contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDtoRegistration)))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//
//        //test
//        Mockito.verify(userManager, Mockito.times(1)).saveUser(any(UserDtoRegistration.class),
//                any(HttpServletRequest.class));
    }

    @Test
    void post() {
    }
}