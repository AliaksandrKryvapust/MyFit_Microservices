package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID> {
}
