package itacad.aliaksandrkryvapust.auditmicroservice.repository.entity;

import lombok.*;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users", schema = "audit")
public class Audit {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String text;
    @Enumerated(EnumType.STRING)
    private Type type;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
}
