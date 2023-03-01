package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IMealRepository extends JpaRepository<Meal, UUID> {
    @Transactional(readOnly = true)
    Page<Meal> findAllByUserId(Pageable pageable, UUID userId);

    @Transactional(readOnly = true)
    Optional<Meal> findByIdAndUserId(UUID uuid, UUID userId);

    @Transactional
    void deleteByIdAndUserId(UUID uuid, UUID userId);
}
