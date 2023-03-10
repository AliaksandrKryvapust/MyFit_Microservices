package itacad.aliaksandrkryvapust.productmicroservice.service.validator.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;

public interface IValidator<TYPE> {
    void validateEntity(TYPE entityToSave);
    void optimisticLockCheck(Long version, TYPE currentEntity);
}
