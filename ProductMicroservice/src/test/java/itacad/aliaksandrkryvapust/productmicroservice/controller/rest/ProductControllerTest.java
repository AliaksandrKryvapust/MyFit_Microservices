package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.UserMapper;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductManager;
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

@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IProductManager productManager;
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
    final String title = "cheese";
    final Integer calories = 300;
    final Double proteins = 33.0;
    final Double fats = 28.0;
    final Double carbohydrates = 0.0;
    final Integer weight = 100;
    final String id = "1d63d7df-f1b3-4e92-95a3-6c7efad96901";
    final String multipleError = "structured_error";

    @Test
    void getPage() throws Exception {
        // preconditions
        final Pageable pageable = PageRequest.of(0, 1, Sort.by("dtCreate").descending());
        final PageDtoOutput<ProductDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        Mockito.when(productManager.getDto(pageable)).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product").param("page", "0")
                        .param("size", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content[*].dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].title").value(title))
                .andExpect(jsonPath("$.content[*].calories").value(calories))
                .andExpect(jsonPath("$.content[*].proteins").value(proteins))
                .andExpect(jsonPath("$.content[*].fats").value(fats))
                .andExpect(jsonPath("$.content[*].carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.content[*].weight").value(weight))
                .andExpect(jsonPath("$.content[*].id").value(id));

        //test
        Mockito.verify(productManager).getDto(pageable);
    }

    @Test
    void get() throws Exception {
        // preconditions
        final ProductDtoOutput productDtoOutput = getPreparedProductDtoOutput();
        Mockito.when(productManager.getDto(UUID.fromString(id))).thenReturn(productDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.calories").value(calories))
                .andExpect(jsonPath("$.proteins").value(proteins))
                .andExpect(jsonPath("$.fats").value(fats))
                .andExpect(jsonPath("$.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.weight").value(weight))
                .andExpect(jsonPath("$.id").value(id));

        //test
        Mockito.verify(productManager).getDto(UUID.fromString(id));
    }

    @Test
    void post() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = getPreparedProductDtoInput();
        final ProductDtoOutput productDtoOutput = getPreparedProductDtoOutput();
        Mockito.when(productManager.saveDto(productDtoInput)).thenReturn(productDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.calories").value(calories))
                .andExpect(jsonPath("$.proteins").value(proteins))
                .andExpect(jsonPath("$.fats").value(fats))
                .andExpect(jsonPath("$.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.weight").value(weight))
                .andExpect(jsonPath("$.id").value(id));

        //test
        Mockito.verify(productManager).saveDto(productDtoInput);
    }

    @Test
    void put() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = getPreparedProductDtoInput();
        final ProductDtoOutput productDtoOutput = getPreparedProductDtoOutput();
        Mockito.when(productManager.updateDto(productDtoInput, UUID.fromString(id), dtUpdate.toEpochMilli()))
                .thenReturn(productDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/product/" + id + "/version/" + dtUpdate.toEpochMilli())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.calories").value(calories))
                .andExpect(jsonPath("$.proteins").value(proteins))
                .andExpect(jsonPath("$.fats").value(fats))
                .andExpect(jsonPath("$.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.weight").value(weight))
                .andExpect(jsonPath("$.id").value(id));

        //test
        Mockito.verify(productManager).updateDto(productDtoInput, UUID.fromString(id), dtUpdate.toEpochMilli());
    }

    @Test
    void delete() throws Exception {
        // preconditions

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/product/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(productManager).deleteDto(UUID.fromString(id));
    }

    @Test
    void validateProductDtoInputEmptyTitle() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(null)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String errorMessage = "title cannot be null";
        final String field = "title";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputShortTitle() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title("a")
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String errorMessage = "title should contain from 2 to 100 letters";
        final String field = "title";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputEmptyCalories() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(null)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String errorMessage = "calories cannot be null";
        final String field = "calories";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputNegativeCalories() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(-1)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String errorMessage = "calories should be positive";
        final String field = "calories";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputEmptyProteins() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(null)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String errorMessage = "proteins cannot be null";
        final String field = "proteins";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputNegativeProteins() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(-1d)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String errorMessage = "proteins should be zero or greater";
        final String field = "proteins";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputEmptyFats() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(null)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String errorMessage = "fats cannot be null";
        final String field = "fats";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputNegativeFats() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(-1d)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .build();
        final String errorMessage = "fats should  be zero or greater";
        final String field = "fats";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputEmptyCarbohydrates() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(null)
                .weight(weight)
                .build();
        final String errorMessage = "carbohydrates cannot be null";
        final String field = "carbohydrates";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputNegativeCarbohydrates() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(-1d)
                .weight(weight)
                .build();
        final String errorMessage = "carbohydrates should  be zero or greater";
        final String field = "carbohydrates";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputEmptyWeight() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(null)
                .build();
        final String errorMessage = "weight cannot be null";
        final String field = "weight";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProductDtoInputNegativeWeight() throws Exception {
        // preconditions
        final ProductDtoInput productDtoInput = ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(-1)
                .build();
        final String errorMessage = "weight should be positive";
        final String field = "weight";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(productManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    private ProductDtoInput getPreparedProductDtoInput() {
        return ProductDtoInput.builder()
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
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

    PageDtoOutput<ProductDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<ProductDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedProductDtoOutput()))
                .build();
    }
}