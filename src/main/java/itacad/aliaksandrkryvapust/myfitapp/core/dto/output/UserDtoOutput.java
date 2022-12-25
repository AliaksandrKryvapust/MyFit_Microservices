package itacad.aliaksandrkryvapust.myfitapp.core.dto.output;

import lombok.*;

@Builder
@Data
public class UserDtoOutput {
    private final @NonNull String mail;
    private final String token;
}
