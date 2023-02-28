package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IProductValidator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;

@Component
public class ProductValidator implements IProductValidator {

    @Override
    public void validateEntity(Product product) {
        checkAuxiliaryFields(product);
        checkTitle(product);
        checkCalories(product);
        checkProteins(product);
        checkFats(product);
        checkCarbohydrates(product);
        checkWeight(product);
    }

    @Override
    public void optimisticLockCheck(Long version, Product currentEntity) {
        Long currentVersion = currentEntity.getDtUpdate().toEpochMilli();
        if (!currentVersion.equals(version)) {
            throw new OptimisticLockException("product table update failed, version does not match update denied");
        }
    }

    @Override
    public void checkCredentials(Product currentEntity) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentEntity.getUserId().equals(userDetails.getId())) {
            throw new BadCredentialsException("It`s forbidden to modify not private data");
        }
    }

    private void checkAuxiliaryFields(Product product) {
        if (product.getId() != null) {
            throw new IllegalStateException("Product id should be empty for product: " + product);
        }
        if (product.getDtUpdate() != null) {
            throw new IllegalStateException("Product version should be empty for product: " + product);
        }
        if (product.getDtCreate() != null) {
            throw new IllegalStateException("Product creation date should be empty for product: " + product);
        }
    }

    private void checkWeight(Product product) {
        if (product.getWeight() == null || product.getWeight() <= 0) {
            throw new IllegalArgumentException("weight is not valid for product:" + product);
        }
    }

    private void checkCarbohydrates(Product product) {
        if (product.getCarbohydrates() == null || product.getCarbohydrates() <= 0) {
            throw new IllegalArgumentException("carbohydrates is not valid for product:" + product);
        }
    }

    private void checkFats(Product product) {
        if (product.getFats() == null || product.getFats() <= 0) {
            throw new IllegalArgumentException("fats is not valid for product:" + product);
        }
    }

    private void checkProteins(Product product) {
        if (product.getProteins() == null || product.getProteins() <= 0) {
            throw new IllegalArgumentException("proteins is not valid for product:" + product);
        }
    }

    private void checkCalories(Product product) {
        if (product.getCalories() == null || product.getCalories() <= 0) {
            throw new IllegalArgumentException("calories is not valid for product:" + product);
        }
    }

    private void checkTitle(Product product) {
        if (product.getTitle() == null || product.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is not valid for product:" + product);
        }
        char[] chars = product.getTitle().toCharArray();
        if (chars.length < 2 || chars.length > 50) {
            throw new IllegalArgumentException("title should contain from 2 to 50 letters for product:" + product);
        }
    }
}
