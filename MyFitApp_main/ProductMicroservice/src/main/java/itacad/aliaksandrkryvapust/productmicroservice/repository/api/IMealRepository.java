package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IMealRepository extends JpaRepository<Meal, UUID> {
    Optional<Meal> findByIdAndUserId(UUID uuid, UUID userId);
}
