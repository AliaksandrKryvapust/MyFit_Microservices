package itacad.aliaksandrkryvapust.myfitapp.core.dto.input;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
@Jacksonized
public class UserDtoLogin {
    @NotNull(message = "username cannot be null")
    @Size(min = 2, max = 50, message = "username should contain from 2 to 50 letters")
    private final String username;
    @NotNull(message = "pizza info cannot be null")
    @Size(min = 2, max = 16, message = "username should contain from 2 to 16 letters")
    private final String password;
}
