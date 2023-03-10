package aliaksandrkryvapust.reportmicroservice.service.validator.api;

public interface IValidator<TYPE> {
    void validateEntity(TYPE entityToSave);
}
