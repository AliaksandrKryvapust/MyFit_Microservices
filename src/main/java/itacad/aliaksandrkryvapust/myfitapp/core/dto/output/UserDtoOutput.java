package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.*;

@Builder
@Data
public class UserDtoOutput {
    private final @NonNull String username;
    private final @NonNull String token;
}
