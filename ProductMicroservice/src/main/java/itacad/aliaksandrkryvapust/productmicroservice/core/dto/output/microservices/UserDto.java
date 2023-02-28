package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class UserDto {
    private final @NonNull String id;
    private final @NonNull String email;
    private final @NonNull String role;
}
