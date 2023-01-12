package aliaksandrkryvapust.reportmicroservice.core.security;

import aliaksandrkryvapust.reportmicroservice.repository.entity.EUserRole;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipal {
    private UUID id;
    private Boolean authenticated;
    private String username;
    private EUserRole role;
    private Instant dtUpdate;
}
