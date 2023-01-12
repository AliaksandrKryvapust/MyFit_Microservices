package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IProductRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
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
        this.validate(product);
        Product currentEntity = this.productRepository.findById(id).orElseThrow();
        this.optimisticLockCheck(version, currentEntity);
        this.checkCredentials(currentEntity);
        this.updateEntityFields(product, currentEntity);
        return this.productRepository.save(currentEntity);
    }

    @Override
    public Page<Product> get(Pageable pageable, UUID userId) {
        return this.productRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public Product get(UUID id, UUID userId) {
        return this.productRepository.findByIdAndUserId(id, userId).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Product currentEntity = this.productRepository.findById(id).orElseThrow();
        this.checkCredentials(currentEntity);
        this.productRepository.deleteById(id);
    }

    @Override
    public List<Product> getByIds(List<UUID> uuids) {
        return this.productRepository.findAllById(uuids);
    }

    private void checkCredentials(Product currentEntity) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentEntity.getUserId().equals(userDetails.getId())){
            throw new BadCredentialsException("It`s forbidden to modify not private data");
        }
    }

    private void optimisticLockCheck(Long version, Product currentEntity) {
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("product table update failed, version does not match update denied");
        }
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
}
