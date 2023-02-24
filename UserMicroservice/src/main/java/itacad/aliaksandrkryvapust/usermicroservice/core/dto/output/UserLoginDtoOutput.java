package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output;

import lombok.*;

@Builder
@Data
public class UserLoginDtoOutput {
    private final @NonNull String email;
    private final @NonNull String token;
}
