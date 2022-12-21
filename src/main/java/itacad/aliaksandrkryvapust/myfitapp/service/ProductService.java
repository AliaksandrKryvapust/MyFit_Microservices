package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IProductRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
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
    public Product save(Product product) {
        validate(product);
        return this.productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product, UUID id, Long version) {
        validate(product);
        Product currentEntity = this.productRepository.findById(id).orElseThrow();
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("product table update failed, version does not match update denied");
        }
        updateEntityFields(product, currentEntity);
        return this.productRepository.save(currentEntity);
    }

    private void validate(Product product) {
        if (product.getId() != null || product.getDtUpdate() != null) {
            throw new IllegalStateException("Product id & version should be empty");
        }
    }

    private void updateEntityFields(Product product, Product currentEntity) {
        currentEntity.setTitle(product.getTitle());
        currentEntity.setCalories(product.getCalories());
        currentEntity.setProteins(product.getProteins());
        currentEntity.setFats(product.getFats());
        currentEntity.setCarbohydrates(product.getCarbohydrates());
        currentEntity.setWeight(product.getWeight());
    }

    @Override
    public Page<Product> get(Pageable pageable) {
        return this.productRepository.findAll(pageable);
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

    @Override
    public List<Product> getByIds(List<UUID> uuids) {
        return this.productRepository.findAllById(uuids);
    }
}
