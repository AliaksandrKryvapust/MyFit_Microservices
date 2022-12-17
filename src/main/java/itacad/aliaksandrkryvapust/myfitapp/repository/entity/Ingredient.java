package itacad.aliaksandrkryvapust.myfitapp.repository.entity;

import lombok.*;

import javax.persistence.*;

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
}
