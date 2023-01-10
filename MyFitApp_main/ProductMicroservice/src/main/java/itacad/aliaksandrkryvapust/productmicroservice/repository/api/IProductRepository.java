package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByIdAndUserId(UUID uuid, UUID userId);
}
