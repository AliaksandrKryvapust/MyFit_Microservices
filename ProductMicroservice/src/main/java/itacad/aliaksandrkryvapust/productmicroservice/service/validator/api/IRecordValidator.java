package itacad.aliaksandrkryvapust.productmicroservice.service.validator.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;

public interface IRecordValidator extends ICredentialsValidator<Record> {
    void validateEntity(Record entityToSave);
}
