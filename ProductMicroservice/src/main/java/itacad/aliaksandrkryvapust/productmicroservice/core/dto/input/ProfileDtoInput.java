package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import itacad.aliaksandrkryvapust.productmicroservice.controller.validator.api.IValidEnum;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EActivityType;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EProfileSex;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Builder
@Data
@Jacksonized
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileDtoInput {
    @NotNull(message = "height cannot be null")
    @Positive(message = "height should be positive")
    private final Integer height;
    @NotNull(message = "weight cannot be null")
    @Positive(message = "weight should be positive")
    private final Double weight;
    @NotNull(message = "birthday cannot be null")
    @Past(message = "birthday should refer to moment in the past")
    private final LocalDate dtBirthday;
    @NotNull(message = "target cannot be null")
    @Positive(message = "target should be positive")
    private final Double target;
    @NotNull(message = "activity type cannot be null")
    @IValidEnum(enumClass = EActivityType.class, message = "activity type does not match")
    private final String activityType;
    @NotNull(message = "sex cannot be null")
    @IValidEnum(enumClass = EProfileSex.class, message = "sex does not match")
    private final String sex;
}
