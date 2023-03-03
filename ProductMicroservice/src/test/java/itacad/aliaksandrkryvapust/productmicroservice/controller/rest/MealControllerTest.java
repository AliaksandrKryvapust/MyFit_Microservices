package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.IngredientDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references.ProductReferenceDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.IngredientDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.UserMapper;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IMealManager;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = MealController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class MealControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IMealManager mealManager;
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
        final PageDtoOutput<MealDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        Mockito.when(mealManager.getDto(pageable)).thenReturn(pageDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipe").param("page", "0")
                        .param("size", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content[*].dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].title").value(title))
                .andExpect(jsonPath("$.content[*].id").value(id))
                .andExpect(jsonPath("$.content[*].composition[*].weight").value(weight))
                .andExpect(jsonPath("$.content[*].composition[*].product.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].composition[*].product.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.content[*].composition[*].product.title").value(title))
                .andExpect(jsonPath("$.content[*].composition[*].product.calories").value(calories))
                .andExpect(jsonPath("$.content[*].composition[*].product.proteins").value(proteins))
                .andExpect(jsonPath("$.content[*].composition[*].product.fats").value(fats))
                .andExpect(jsonPath("$.content[*].composition[*].product.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.content[*].composition[*].product.weight").value(weight))
                .andExpect(jsonPath("$.content[*].composition[*].product.id").value(id));

        //test
        Mockito.verify(mealManager).getDto(pageable);
    }

    @Test
    void get() throws Exception {
        // preconditions
        final MealDtoOutput mealDtoOutput = getPreparedMealDtoOutput();
        Mockito.when(mealManager.getDto(UUID.fromString(id))).thenReturn(mealDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/recipe/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.composition[*].weight").value(weight))
                .andExpect(jsonPath("$.composition[*].product.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.composition[*].product.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.composition[*].product.title").value(title))
                .andExpect(jsonPath("$.composition[*].product.calories").value(calories))
                .andExpect(jsonPath("$.composition[*].product.proteins").value(proteins))
                .andExpect(jsonPath("$.composition[*].product.fats").value(fats))
                .andExpect(jsonPath("$.composition[*].product.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.composition[*].product.weight").value(weight))
                .andExpect(jsonPath("$.composition[*].product.id").value(id));

        //test
        Mockito.verify(mealManager).getDto(UUID.fromString(id));
    }

    @Test
    void post() throws Exception {
        // preconditions
        final MealDtoInput mealDtoInput = getPreparedMealDtoInput();
        final MealDtoOutput mealDtoOutput = getPreparedMealDtoOutput();
        Mockito.when(mealManager.saveDto(mealDtoInput)).thenReturn(mealDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.composition[*].weight").value(weight))
                .andExpect(jsonPath("$.composition[*].product.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.composition[*].product.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.composition[*].product.title").value(title))
                .andExpect(jsonPath("$.composition[*].product.calories").value(calories))
                .andExpect(jsonPath("$.composition[*].product.proteins").value(proteins))
                .andExpect(jsonPath("$.composition[*].product.fats").value(fats))
                .andExpect(jsonPath("$.composition[*].product.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.composition[*].product.weight").value(weight))
                .andExpect(jsonPath("$.composition[*].product.id").value(id));

        //test
        Mockito.verify(mealManager).saveDto(mealDtoInput);
    }

    @Test
    void put() throws Exception {
        // preconditions
        final MealDtoInput mealDtoInput = getPreparedMealDtoInput();
        final MealDtoOutput mealDtoOutput = getPreparedMealDtoOutput();
        Mockito.when(mealManager.updateDto(mealDtoInput, UUID.fromString(id), dtUpdate.toEpochMilli()))
                .thenReturn(mealDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/recipe/" + id + "/version/" + dtUpdate.toEpochMilli())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.composition[*].weight").value(weight))
                .andExpect(jsonPath("$.composition[*].product.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.composition[*].product.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.composition[*].product.title").value(title))
                .andExpect(jsonPath("$.composition[*].product.calories").value(calories))
                .andExpect(jsonPath("$.composition[*].product.proteins").value(proteins))
                .andExpect(jsonPath("$.composition[*].product.fats").value(fats))
                .andExpect(jsonPath("$.composition[*].product.carbohydrates").value(carbohydrates))
                .andExpect(jsonPath("$.composition[*].product.weight").value(weight))
                .andExpect(jsonPath("$.composition[*].product.id").value(id));

        //test
        Mockito.verify(mealManager).updateDto(mealDtoInput, UUID.fromString(id), dtUpdate.toEpochMilli());
    }

    @Test
    void delete() throws Exception {
        // preconditions

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/recipe/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //test
        Mockito.verify(mealManager).deleteDto(UUID.fromString(id));
    }

    @Test
    void validateMealDtoInputEmptyComposition() throws Exception {
        // preconditions
        final MealDtoInput mealDtoInput = MealDtoInput.builder()
                .title(title)
                .composition(null)
                .build();
        final String errorMessage = "composition cannot be null";
        final String field = "composition";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(mealManager, Mockito.times(0)).saveDto(mealDtoInput);
    }

    @Test
    void validateMealDtoInputEmptyTitle() throws Exception {
        // preconditions
        final MealDtoInput mealDtoInput = MealDtoInput.builder()
                .title(null)
                .composition(Collections.singleton(getPreparedIngredientDtoInput()))
                .build();
        final String errorMessage = "title cannot be null";
        final String field = "title";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(mealManager, Mockito.times(0)).saveDto(mealDtoInput);
    }

    @Test
    void validateMealDtoInputShortTitle() throws Exception {
        // preconditions
        final MealDtoInput mealDtoInput = MealDtoInput.builder()
                .title("a")
                .composition(Collections.singleton(getPreparedIngredientDtoInput()))
                .build();
        final String errorMessage = "title should contain from 2 to 100 letters";
        final String field = "title";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(mealManager, Mockito.times(0)).saveDto(mealDtoInput);
    }

    @Test
    void validateMealDtoInputEmptyProductReference() throws Exception {
        // preconditions
        final IngredientDtoInput ingredientDtoInput = IngredientDtoInput.builder()
                .product(null)
                .weight(weight)
                .build();
        final MealDtoInput mealDtoInput = MealDtoInput.builder()
                .title(title)
                .composition(Collections.singleton(ingredientDtoInput))
                .build();
        final String errorMessage = "product reference cannot be null";
        final String field = "composition[].product";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(mealManager, Mockito.times(0)).saveDto(mealDtoInput);
    }

    @Test
    void validateMealDtoInputEmptyIngredientWeight() throws Exception {
        // preconditions
        final IngredientDtoInput ingredientDtoInput = IngredientDtoInput.builder()
                .product(getPreparedProductReferenceDtoInput())
                .weight(null)
                .build();
        final MealDtoInput mealDtoInput = MealDtoInput.builder()
                .title(title)
                .composition(Collections.singleton(ingredientDtoInput))
                .build();
        final String errorMessage = "weight cannot be null";
        final String field = "composition[].weight";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(mealManager, Mockito.times(0)).saveDto(mealDtoInput);
    }

    @Test
    void validateMealDtoInputNegativeIngredientWeight() throws Exception {
        // preconditions
        final IngredientDtoInput ingredientDtoInput = IngredientDtoInput.builder()
                .product(getPreparedProductReferenceDtoInput())
                .weight(-1)
                .build();
        final MealDtoInput mealDtoInput = MealDtoInput.builder()
                .title(title)
                .composition(Collections.singleton(ingredientDtoInput))
                .build();
        final String errorMessage = "weight should be positive";
        final String field = "composition[].weight";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(mealManager, Mockito.times(0)).saveDto(mealDtoInput);
    }

    @Test
    void validateMealDtoInputEmptyProductId() throws Exception {
        // preconditions
        final ProductReferenceDtoInput dtoInput = ProductReferenceDtoInput.builder()
                .id(null)
                .build();
        final IngredientDtoInput ingredientDtoInput = IngredientDtoInput.builder()
                .product(dtoInput)
                .weight(weight)
                .build();
        final MealDtoInput mealDtoInput = MealDtoInput.builder()
                .title(title)
                .composition(Collections.singleton(ingredientDtoInput))
                .build();
        final String errorMessage = "product reference cannot be blank";
        final String field = "composition[].product.id";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(mealManager, Mockito.times(0)).saveDto(mealDtoInput);
    }

    @Test
    void validateMealDtoInputBlankProductId() throws Exception {
        // preconditions
        final ProductReferenceDtoInput dtoInput = ProductReferenceDtoInput.builder()
                .id("  ")
                .build();
        final IngredientDtoInput ingredientDtoInput = IngredientDtoInput.builder()
                .product(dtoInput)
                .weight(weight)
                .build();
        final MealDtoInput mealDtoInput = MealDtoInput.builder()
                .title(title)
                .composition(Collections.singleton(ingredientDtoInput))
                .build();
        final String errorMessage = "product reference cannot be blank";
        final String field = "composition[].product.id";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/recipe").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(mealManager, Mockito.times(0)).saveDto(mealDtoInput);
    }

    private ProductReferenceDtoInput getPreparedProductReferenceDtoInput() {
        return ProductReferenceDtoInput.builder()
                .id(id)
                .build();
    }

    private IngredientDtoInput getPreparedIngredientDtoInput() {
        ProductReferenceDtoInput dtoInput = getPreparedProductReferenceDtoInput();
        return IngredientDtoInput.builder()
                .product(dtoInput)
                .weight(weight)
                .build();
    }

    private MealDtoInput getPreparedMealDtoInput() {
        Set<IngredientDtoInput> ingredients = Collections.singleton(getPreparedIngredientDtoInput());
        return MealDtoInput.builder()
                .title(title)
                .composition(ingredients)
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

    private IngredientDtoOutput getPreparedIngredientDtoOutput() {
        ProductDtoOutput dtoOutput = getPreparedProductDtoOutput();
        return IngredientDtoOutput.builder()
                .product(dtoOutput)
                .weight(weight)
                .build();
    }

    private MealDtoOutput getPreparedMealDtoOutput() {
        List<IngredientDtoOutput> ingredients = Collections.singletonList(getPreparedIngredientDtoOutput());
        return MealDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .title(title)
                .composition(ingredients)
                .id(id)
                .build();
    }

    PageDtoOutput<MealDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<MealDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedMealDtoOutput()))
                .build();
    }
}