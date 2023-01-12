package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IMealRepository extends JpaRepository<Meal, UUID> {
    Page<Meal> findAllByUserId(Pageable pageable, UUID userId);
    Optional<Meal> findByIdAndUserId(UUID uuid, UUID userId);
}
