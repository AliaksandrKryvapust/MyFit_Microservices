package itacad.aliaksandrkryvapust.myfitapp.core.dto.input;

import itacad.aliaksandrkryvapust.myfitapp.controller.validator.api.IValidEmail;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Builder
@Data
@Jacksonized
public class UserDtoRegistration {
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
}
