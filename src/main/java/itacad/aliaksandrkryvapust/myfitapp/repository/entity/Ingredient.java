package itacad.aliaksandrkryvapust.myfitapp.repository.entity;

import lombok.*;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ingredients", schema = "app")
public class Ingredient {
    @EmbeddedId
    private IngredientKey id;
    @ManyToOne
    @MapsId("mealId")
    @JoinColumn(name = "meal_id", referencedColumnName = "id")
    @Setter
    private Meal meal;
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @Setter
    private Product product;
    @Setter
    private Double weight;
    @Column(updatable = false)
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;
}
