package itacad.aliaksandrkryvapust.myfitapp.repository.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class IngredientKey implements Serializable {
    private UUID productId;
    private UUID mealId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientKey that = (IngredientKey) o;
        return Objects.equals(getProductId(), that.getProductId()) && Objects.equals(getMealId(), that.getMealId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getMealId());
    }
}
