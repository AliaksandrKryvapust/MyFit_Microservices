package itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input;

import itacad.aliaksandrkryvapust.auditmicroservice.controller.validator.api.IValidEnum;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserRole;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
@Jacksonized
public class TokenValidationDto {
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
}
