package itacad.aliaksandrkryvapust.auditmicroservice.service.validator.api;

public interface IValidator<TYPE> {
    void validateEntity(TYPE entityToSave);
}
