package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output;

import lombok.*;

@Builder
@Data
public class UserLoginDtoOutput {
    private final @NonNull String mail;
    private final String token;
}
