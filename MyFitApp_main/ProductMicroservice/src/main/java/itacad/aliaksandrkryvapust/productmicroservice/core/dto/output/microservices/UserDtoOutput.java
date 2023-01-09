package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class UserDtoOutput {
    private final @NonNull String mail;
    private final @NonNull UserRole role;
}
