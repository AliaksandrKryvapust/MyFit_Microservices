package itacad.aliaksandrkryvapust.productmicroservice.service.validator.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;

public interface IRecordValidator {
    void validateEntity(Record entityToSave);
}
