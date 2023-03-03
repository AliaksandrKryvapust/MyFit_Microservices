package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references.MealReferenceDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.RecordDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.ParamsMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.UserMapper;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IRecordManager;
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

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = RecordController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class RecordControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IRecordManager recordManager;
    @MockBean
    private ParamsMapper paramsMapper;
    @MockBean
    SecurityContext securityContext;
    // Beans for JwtFilter
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private TokenMapper tokenMapper;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String id = "1d63d7df-f1b3-4e92-95a3-6c7efad96901";
    final Integer weight = 105;
    final String title = "cheese";
    final Integer calories = 300;
    final Double proteins = 33.0;
    final Double fats = 28.0;
    final Double carbohydrates = 0.0;
    final String dt_Supply = "1993-07-01";
    final String multipleError = "structured_error";


    @Test
    void getPage() throws Exception {
        // preconditions
        final Pageable pageable = PageRequest.of(0, 1, Sort.by("dtCreate").descending());
        final PageDtoOutput<RecordDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        Mockito.when(recordManager.getDto(pageable, UUID.fromString(id))).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profile/" + id + "/journal/food").param("page", "0")
                        .param("size", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content[*].dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].dt_supply").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].weight").value(weight))
                .andExpect(jsonPath("$.content[*].id").value(id))
                .andExpect(jsonPath("$.content[*].product.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].product.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].product.title").value(title))
                .andExpect(jsonPath("$.content[*].product.calories").value(calories))
                .andExpect(jsonPath("$.content[*].product.proteins").value(proteins))
                .andExpect(jsonPath("$.content[*].product.fats").value(fats))
                .andExpect(jsonPath("$.content[*].product.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.content[*].product.weight").value(weight))
                .andExpect(jsonPath("$.content[*].product.id").value(id));

        //test
        Mockito.verify(recordManager).getDto(pageable, UUID.fromString(id));
    }

    @Test
    void post() throws Exception {
        // preconditions
        final RecordDtoInput recordDtoInput = getPreparedRecordDtoInput();
        final RecordDtoOutput recordDtoOutput = getPreparedRecordDtoOutput();
        Mockito.when(recordManager.saveDto(recordDtoInput, UUID.fromString(id))).thenReturn(recordDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile/" + id + "/journal/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_supply").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.weight").value(weight))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.product.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.product.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.product.title").value(title))
                .andExpect(jsonPath("$.product.calories").value(calories))
                .andExpect(jsonPath("$.product.proteins").value(proteins))
                .andExpect(jsonPath("$.product.fats").value(fats))
                .andExpect(jsonPath("$.product.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.product.weight").value(weight))
                .andExpect(jsonPath("$.product.id").value(id));

        //test
        Mockito.verify(recordManager).saveDto(recordDtoInput, UUID.fromString(id));
    }

    @Test
    void export() throws Exception {
        // preconditions
        final ParamsDto paramsDto = getPreparedParamsDto();
        final RecordDto recordDto = getPreparedRecordDto();
//        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
        Mockito.when(paramsMapper.getParamsDto(any(HttpServletRequest.class))).thenReturn(paramsDto);
        Mockito.when(recordManager.getRecord(paramsDto)).thenReturn(Collections.singletonList(recordDto));

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/journal/food/export").param("from", "2022-12-21")
                        .param("to", "2023-01-12"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[*].dt_supply").value(dt_Supply))
                .andExpect(jsonPath("$.[*].weight").value(weight))
                .andExpect(jsonPath("$.[*].product.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.[*].product.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.[*].product.title").value(title))
                .andExpect(jsonPath("$.[*].product.calories").value(calories))
                .andExpect(jsonPath("$.[*].product.proteins").value(proteins))
                .andExpect(jsonPath("$.[*].product.fats").value(fats))
                .andExpect(jsonPath("$.[*].product.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.[*].product.weight").value(weight))
                .andExpect(jsonPath("$.[*].product.id").value(id));

        //test
        Mockito.verify(recordManager).getRecord(paramsDto);
    }

    @Test
    void validateRecordDtoInputEmptySupplyDate() throws Exception {
        // preconditions
        final RecordDtoInput recordDtoInput = RecordDtoInput.builder()
                .dtSupply(null)
                .recipe(getPreparedMealReferenceDtoInput())
                .weight(weight)
                .build();
        final String errorMessage = "supply date cannot be null";
        final String field = "dtSupply";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile/" + id + "/journal/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(recordManager, Mockito.times(0)).saveDto(recordDtoInput, UUID.fromString(id));
    }

    @Test
    void validateRecordDtoInputEmptyWeight() throws Exception {
        // preconditions
        final RecordDtoInput recordDtoInput = RecordDtoInput.builder()
                .dtSupply(dtUpdate)
                .recipe(getPreparedMealReferenceDtoInput())
                .weight(null)
                .build();
        final String errorMessage = "weight cannot be null";
        final String field = "weight";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile/" + id + "/journal/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(recordManager, Mockito.times(0)).saveDto(recordDtoInput, UUID.fromString(id));
    }

    @Test
    void validateRecordDtoInputNegativeWeight() throws Exception {
        // preconditions
        final RecordDtoInput recordDtoInput = RecordDtoInput.builder()
                .dtSupply(dtUpdate)
                .recipe(getPreparedMealReferenceDtoInput())
                .weight(-1)
                .build();
        final String errorMessage = "weight should be positive";
        final String field = "weight";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile/" + id + "/journal/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(recordManager, Mockito.times(0)).saveDto(recordDtoInput, UUID.fromString(id));
    }

    @Test
    void validateRecordDtoInputEmptyMealId() throws Exception {
        // preconditions
        final MealReferenceDtoInput mealReferenceDtoInput = MealReferenceDtoInput.builder()
                .id("   ")
                .build();
        final RecordDtoInput recordDtoInput = RecordDtoInput.builder()
                .dtSupply(dtUpdate)
                .recipe(mealReferenceDtoInput)
                .weight(weight)
                .build();
        final String errorMessage = "recipe cannot be blank";
        final String field = "recipe.id";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile/" + id + "/journal/food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(recordManager, Mockito.times(0)).saveDto(recordDtoInput, UUID.fromString(id));
    }

    private ParamsDto getPreparedParamsDto() {
        return ParamsDto.builder()
                .userId(id)
                .from(dtCreate)
                .to(dtUpdate)
                .build();
    }

    private RecordDto getPreparedRecordDto() {
        return RecordDto.builder()
                .weight(weight)
                .dtSupply(dt_Supply)
                .product(getPreparedProductDtoOutput())
                .build();
    }

    private MealReferenceDtoInput getPreparedMealReferenceDtoInput() {
        return MealReferenceDtoInput.builder()
                .id(id)
                .build();
    }

    private RecordDtoInput getPreparedRecordDtoInput() {
        return RecordDtoInput.builder()
                .dtSupply(dtUpdate)
                .recipe(getPreparedMealReferenceDtoInput())
                .weight(weight)
                .build();
    }

    private ProductDtoOutput getPreparedProductDtoOutput() {
        return ProductDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .id(id)
                .build();
    }

    private RecordDtoOutput getPreparedRecordDtoOutput() {
        return RecordDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtSupply(dtUpdate)
                .product(getPreparedProductDtoOutput())
                .weight(weight)
                .id(id)
                .build();
    }

    PageDtoOutput<RecordDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<RecordDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedRecordDtoOutput()))
                .build();
    }
}