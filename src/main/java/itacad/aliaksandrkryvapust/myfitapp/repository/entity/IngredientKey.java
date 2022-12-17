package itacad.aliaksandrkryvapust.myfitapp.repository.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
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
}
