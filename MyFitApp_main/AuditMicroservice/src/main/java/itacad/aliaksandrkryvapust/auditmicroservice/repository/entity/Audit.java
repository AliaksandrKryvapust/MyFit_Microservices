package itacad.aliaksandrkryvapust.auditmicroservice.repository.entity;

import lombok.*;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "audit", schema = "audit")
public class Audit {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Setter
    private UUID uuid;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Setter
    private User user;
    @Setter
    private String text;
    @Enumerated(EnumType.STRING)
    @Setter
    private Type type;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    @Setter
    private Instant dtCreate;
}
