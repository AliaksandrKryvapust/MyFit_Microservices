package itacad.aliaksandrkryvapust.productmicroservice.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class User {
    private UUID user_id;
    @Column(nullable = false, unique = true)
    private String username;
    private Instant version;
}
