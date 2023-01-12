package aliaksandrkryvapust.reportmicroservice.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class User {
    private UUID userId;
    @Column(nullable = false, unique = true)
    private String username;
    @Enumerated(EnumType.STRING)
    private EUserRole role;
}
