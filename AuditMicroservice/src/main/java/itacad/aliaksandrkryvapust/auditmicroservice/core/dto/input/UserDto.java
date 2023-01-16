package itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input;

import itacad.aliaksandrkryvapust.auditmicroservice.controller.validator.api.IValidEnum;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EUserRole;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@Jacksonized
public class UserDto {
    @NotNull(message = "id cannot be null")
    @NotBlank(message = "id cannot be empty")
    private final UUID uuid;
    @Nullable
    private final String nick;
    @NotNull(message = "mail cannot be null")
    @Size(min = 2, max = 100, message = "mail should contain from 2 to 100 letters")
    private final String mail;
    @NotNull(message = "user role cannot be null")
    @IValidEnum(enumClass = EUserRole.class, message = "user role does not match")
    private final String role;
    @Nullable
    private final String status;
    @Nullable
    private final Instant dtCreate;
    @Nullable
    private final Instant dtUpdate;
}
