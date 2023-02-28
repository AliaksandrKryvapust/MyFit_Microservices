package itacad.aliaksandrkryvapust.productmicroservice.service.validator.api;

public interface ICredentialsValidator<TYPE> {
    void checkCredentials(TYPE currentEntity);
}
