package aliaksandrkryvapust.reportmicroservice.controller.rest;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.FileDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ParamsDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.mapper.microservices.UserMapper;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EFileType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportManager;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.XLSX_CONTENT_TYPE;


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

    // preconditions
    final LocalDate from = LocalDate.parse("2022-12-21");
    final LocalDate to = LocalDate.parse("2023-01-12");
    final Long dtCreate = 1673532204657L;
    final Long dtUpdate = 1673532532870L;
    final String description = "description";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final EFileType fileType = EFileType.JOURNAL_REPORT;
    final String url = "https://www.example.com";

    @Test
    @WithMockUser(username = "admin@myfit.com", password = "kdrL556D", roles = {"ADMIN"})
    void getPage() throws Exception {
        // preconditions
        final Pageable pageable = PageRequest.of(0, 5, Sort.by("dtCreate").descending());
        final PageDtoOutput<ReportDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        Mockito.when(reportManager.getDto(pageable)).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/report").param("page", "0")
                        .param("size", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_create").value(dtCreate))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].dt_update").value(dtUpdate))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].id").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].status").value(EStatus.LOADED.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].type").value(EType.JOURNAL_FOOD.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].description").value(description))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].params.from").value(from.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[*].params.to").value(to.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_pages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_elements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.first").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.number_of_elements").value(1));

        //test
        Mockito.verify(reportManager).getDto(pageable);
    }

    @Test
    void export() throws Exception {
        // preconditions
        final FileDtoOutput dtoOutput = getPreparedFileDtoOutput();
        Mockito.when(reportManager.getReportFile(id)).thenReturn(dtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/report/" + id + "/export"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.file_type").value(fileType.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content_type").value(XLSX_CONTENT_TYPE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.file_name").value(id.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value(url));

        //test
        Mockito.verify(reportManager, Mockito.times(1)).getReportFile(id);
    }

    @Test
    void post() throws Exception {
        // preconditions
        final ParamsDto paramsDto = getPreparedParamsDto();

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/report/" + EType.JOURNAL_FOOD.name()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paramsDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        //test
        Mockito.verify(reportManager, Mockito.times(1)).saveDto(paramsDto, EType.JOURNAL_FOOD);
    }

    private ParamsDtoOutput getPreparedParamsDtoOutput() {
        return ParamsDtoOutput.builder()
                .from(from)
                .to(to)
                .build();
    }

    private ReportDtoOutput getPreparedReportDtoOutput() {
        return ReportDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .id(id.toString())
                .status(EStatus.LOADED.name())
                .type(EType.JOURNAL_FOOD.name())
                .description(description)
                .params(getPreparedParamsDtoOutput())
                .build();
    }

    PageDtoOutput<ReportDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<ReportDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedReportDtoOutput()))
                .build();
    }

    private FileDtoOutput getPreparedFileDtoOutput() {
        return FileDtoOutput.builder()
                .fileType(fileType.name())
                .contentType(XLSX_CONTENT_TYPE)
                .fileName(id.toString())
                .url(url)
                .build();
    }

    private ParamsDto getPreparedParamsDto() {
        return ParamsDto.builder()
                .from(from)
                .to(to)
                .build();
    }
}