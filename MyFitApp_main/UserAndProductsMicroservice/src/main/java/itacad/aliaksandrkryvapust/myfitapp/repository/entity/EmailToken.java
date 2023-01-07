package itacad.aliaksandrkryvapust.myfitapp.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "token", schema = "users")
public class EmailToken {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String token;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @org.hibernate.annotations.Generated(GenerationTime.ALWAYS)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;
}
