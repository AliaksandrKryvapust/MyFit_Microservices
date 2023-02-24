package itacad.aliaksandrkryvapust.usermicroservice.service.validator.api;

public interface IValidator <TYPE> {
    void validateEntity(TYPE type);
    void optimisticLockCheck(Long version, TYPE currentEntity);
}
