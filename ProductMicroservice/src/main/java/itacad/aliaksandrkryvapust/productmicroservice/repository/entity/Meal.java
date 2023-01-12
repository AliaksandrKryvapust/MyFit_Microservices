package itacad.aliaksandrkryvapust.productmicroservice.repository.entity;

import lombok.*;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "meal", schema = "app")
public class Meal {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "meal_id", referencedColumnName = "id", nullable = false)
    @Setter
    private List<Ingredient> ingredients;
    @Setter
    private String title;
    @Setter
    private UUID userId;
    @Column(updatable = false)
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;
}
