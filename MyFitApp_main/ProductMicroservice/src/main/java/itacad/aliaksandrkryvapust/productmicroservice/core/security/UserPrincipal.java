package itacad.aliaksandrkryvapust.productmicroservice.core.security;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal {
    private UUID id;
    private Boolean authenticated;
    private String username;
    private EUserRole role;
    private Instant dtUpdate;
}
