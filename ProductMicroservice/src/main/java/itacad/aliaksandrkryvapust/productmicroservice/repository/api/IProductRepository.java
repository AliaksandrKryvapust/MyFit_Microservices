package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID> {
    @Transactional(readOnly = true)
    Page<Product> findAllByUserId(Pageable pageable, UUID userId);

    @Transactional(readOnly = true)
    Optional<Product> findByIdAndUserId(UUID uuid, UUID userId);

    @Transactional
    void deleteByIdAndUserId(UUID uuid, UUID userId);
}
