package itacad.aliaksandrkryvapust.myfitapp.repository.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IIngredientRepository extends JpaRepository<Ingredient, UUID> {
}
