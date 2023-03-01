package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface IIngredientRepository extends JpaRepository<Ingredient, UUID> {
    @Transactional
    void deleteAllByMealId(UUID uuid);
}
