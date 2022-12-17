package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IProductRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService implements IProductService {
    private final IProductRepository productRepository;

    @Autowired
    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product save(Product menuItem) {
        if (menuItem.getId() != null || menuItem.getDtUpdate() != null) {
            throw new IllegalStateException("Product id & version should be empty");
        }
        return this.productRepository.save(menuItem);
    }

    @Override
    @Transactional
    public Product update(Product menuItem, UUID id, Instant version) {
        if (menuItem.getId() != null || menuItem.getDtUpdate() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        Product currentEntity = this.productRepository.findById(id).orElseThrow();
        if (!currentEntity.getDtUpdate().equals(version)) {
            throw new OptimisticLockException("menu_item table update failed, version does not match update denied");
        }
        return this.productRepository.save(currentEntity);
    }

    @Override
    public List<Product> get() {
        return this.productRepository.findAll();
    }

    @Override
    public Product get(UUID id) {
        return this.productRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        this.productRepository.deleteById(id);
    }
}
