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
@Table(name = "ingredients", schema = "app")
public class Ingredient {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "product_id", insertable = false, updatable = false)
    private UUID productId;
    @Column(name = "meal_id", insertable = false, updatable = false)
    private UUID mealId;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @Setter
    private Product product;
    @Setter
    private Integer weight;
    @Column(updatable = false)
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;
}
