package itacad.aliaksandrkryvapust.usermicroservice.core.dto.input;

import itacad.aliaksandrkryvapust.usermicroservice.controller.validator.api.IValidEmail;
import itacad.aliaksandrkryvapust.usermicroservice.controller.validator.api.IValidEnum;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserRole;
import itacad.aliaksandrkryvapust.usermicroservice.repository.entity.UserStatus;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
@Jacksonized
public class UserDtoInput {
    @NotNull(message = "username cannot be null")
    @Size(min = 2, max = 50, message = "username should contain from 2 to 50 letters")
    private final String nick;
    @NotNull(message = "password cannot be null")
    @Size(min = 2, max = 200, message = "password should contain from 2 to 200 letters")
    private final String password;
    @NotNull(message = "email cannot be null")
    @NotEmpty
    @IValidEmail(message = "email is not valid")
    private final String mail;
    @NotNull(message = "user role cannot be null")
    @IValidEnum(enumClass = UserRole.class, message = "user role does not match")
    private final String role;
    @NotNull(message = "user role cannot be null")
    @IValidEnum(enumClass = UserStatus.class, message = "user status does not match")
    private final String status;
}
