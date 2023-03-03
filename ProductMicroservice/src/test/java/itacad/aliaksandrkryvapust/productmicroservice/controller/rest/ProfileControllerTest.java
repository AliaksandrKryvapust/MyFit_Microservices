package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProfileDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProfileDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.UserDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.TokenMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.UserMapper;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EActivityType;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EProfileSex;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ProfileController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class ProfileControllerTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private IProfileManager profileManager;
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
    final Integer height = 173;
    final Double weight = 105.3;
    final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    final LocalDate dtBirthday = LocalDate.parse("1993-07-01", df);
    final String dt_Birthday = "1993-07-01";
    final Double target = 85.9;
    final EActivityType activity_type = EActivityType.ACTIVE;
    final EProfileSex sex = EProfileSex.MALE;
    final String username = "someone";
    final String multipleError = "structured_error";

    @Test
    void get() throws Exception {
        // preconditions
        final ProfileDtoOutput profileDtoOutput = getPreparedProfileDtoOutput();
        Mockito.when(profileManager.getDto(UUID.fromString(id))).thenReturn(profileDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profile/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.user.version").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.user.username").value(username))
                .andExpect(jsonPath("$.user.user_id").value(id))
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.sex").value(sex.name()))
                .andExpect(jsonPath("$.activity_type").value(activity_type.name()))
                .andExpect(jsonPath("$.target").value(target))
                .andExpect(jsonPath("$.dt_birthday").value(dt_Birthday))
                .andExpect(jsonPath("$.weight").value(weight))
                .andExpect(jsonPath("$.height").value(height))
                .andExpect(jsonPath("$.id").value(id));

        //test
        Mockito.verify(profileManager).getDto(UUID.fromString(id));
    }

    @Test
    void post() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = getPreparedProfileDtoInput();
        final ProfileDtoOutput productDtoOutput = getPreparedProfileDtoOutput();
        Mockito.when(profileManager.saveDto(productDtoInput)).thenReturn(productDtoOutput);

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.user.version").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.user.username").value(username))
                .andExpect(jsonPath("$.user.user_id").value(id))
                .andExpect(jsonPath("$.dt_create").value(dtCreate.toEpochMilli()))
                .andExpect(jsonPath("$.dt_update").value(dtUpdate.toEpochMilli()))
                .andExpect(jsonPath("$.sex").value(sex.name()))
                .andExpect(jsonPath("$.activity_type").value(activity_type.name()))
                .andExpect(jsonPath("$.target").value(target))
                .andExpect(jsonPath("$.dt_birthday").value(dt_Birthday))
                .andExpect(jsonPath("$.weight").value(weight))
                .andExpect(jsonPath("$.height").value(height))
                .andExpect(jsonPath("$.id").value(id));

        //test
        Mockito.verify(profileManager).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputEmptyHeight() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(null)
                .build();
        final String errorMessage = "height cannot be null";
        final String field = "height";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputNegativeHeight() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(-1)
                .build();
        final String errorMessage = "height should be positive";
        final String field = "height";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputEmptyWeight() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(null)
                .height(height)
                .build();
        final String errorMessage = "weight cannot be null";
        final String field = "weight";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputNegativeWeight() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(-1d)
                .height(height)
                .build();
        final String errorMessage = "weight should be positive";
        final String field = "weight";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputEmptyBirthday() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(null)
                .weight(weight)
                .height(height)
                .build();
        final String errorMessage = "birthday cannot be null";
        final String field = "dtBirthday";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputFutureBirthday() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(LocalDate.parse("2223-07-01", df))
                .weight(weight)
                .height(height)
                .build();
        final String errorMessage = "birthday should refer to moment in the past";
        final String field = "dtBirthday";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputEmptyTarget() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(null)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String errorMessage = "target cannot be null";
        final String field = "target";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputNegativeTarget() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(-1d)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String errorMessage = "target should be positive";
        final String field = "target";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputEmptyActivity() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(null)
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String errorMessage = "activity type cannot be null";
        final String field = "activityType";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputWrongActivity() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType("activity")
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String errorMessage = "activity type does not match";
        final String field = "activityType";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }


    @Test
    void validateProfileDtoInputEmptySex() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex(null)
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String errorMessage = "sex cannot be null";
        final String field = "sex";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    @Test
    void validateProfileDtoInputWrongSex() throws Exception {
        // preconditions
        final ProfileDtoInput productDtoInput = ProfileDtoInput.builder()
                .sex("sex")
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
        final String errorMessage = "sex does not match";
        final String field = "sex";

        // assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/profile").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDtoInput)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.logref").value(multipleError))
                .andExpect(jsonPath("$.errors[*].field").value(field))
                .andExpect(jsonPath("$.errors[*].message").value(errorMessage));

        //test
        Mockito.verify(profileManager, Mockito.times(0)).saveDto(productDtoInput);
    }

    private UserDtoOutput getPreparedUserDtoOutput() {
        return UserDtoOutput.builder()
                .version(dtUpdate)
                .username(username)
                .user_id(id)
                .build();
    }

    private ProfileDtoOutput getPreparedProfileDtoOutput() {
        return ProfileDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .user(getPreparedUserDtoOutput())
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .id(id)
                .build();
    }

    private ProfileDtoInput getPreparedProfileDtoInput() {
        return ProfileDtoInput.builder()
                .sex(sex.name())
                .activityType(activity_type.name())
                .target(target)
                .dtBirthday(dtBirthday)
                .weight(weight)
                .height(height)
                .build();
    }
}