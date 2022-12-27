package itacad.aliaksandrkryvapust.myfitapp.repository.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID> {
}
