package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import itacad.aliaksandrkryvapust.productmicroservice.controller.validator.api.IValidEnum;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EUserRole;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.Instant;

@Builder
@Data
@Jacksonized
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenValidationDto {
    @NotNull(message = "id cannot be null")
    @NotBlank(message = "id cannot be blank")
    private final String id;
    @NotNull(message = "authenticated cannot be null")
    private final Boolean authenticated;
    @NotNull(message = "username cannot be null")
    @Size(min = 2, max = 100, message = "username should contain from 2 to 100 letters")
    private final String username;
    @NotNull(message = "user role cannot be null")
    @IValidEnum(enumClass = EUserRole.class, message = "user role does not match")
    private final String role;
    @NotNull(message = "dtUpdate cannot be null")
    @Past(message = "dtUpdate should refer to moment in the past")
    private final Instant dtUpdate;
}

