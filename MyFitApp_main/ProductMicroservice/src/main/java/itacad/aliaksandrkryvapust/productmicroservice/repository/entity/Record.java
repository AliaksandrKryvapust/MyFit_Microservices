package itacad.aliaksandrkryvapust.productmicroservice.repository.entity;

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
@Table(name = "records", schema = "app")
public class Record {
    @Id
    @GeneratedValue(generator = "uuid3")
    @GenericGenerator(name = "uuid3", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "product_id", insertable = false, updatable = false)
    private UUID productId;
    @Column(name = "meal_id", insertable = false, updatable = false)
    private UUID mealId;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @Setter
    private Product product;
    @OneToOne
    @JoinColumn(name = "meal_id", referencedColumnName = "id")
    @Setter
    private Meal meal;
    @Setter
    private Instant dtSupply;
    @Setter
    private UUID userId;
    @Setter
    private Integer weight;
    @Column(updatable = false)
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
}
