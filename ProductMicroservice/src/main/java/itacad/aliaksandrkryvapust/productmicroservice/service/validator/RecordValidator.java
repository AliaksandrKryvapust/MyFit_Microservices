package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IRecordValidator;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RecordValidator implements IRecordValidator {

    @Override
    public void validateEntity(Record record) {
        checkAuxiliaryFields(record);
        checkDtSupply(record);
        checkWeight(record);
    }


    private void checkAuxiliaryFields(Record record) {
        if (record.getId() != null) {
            throw new IllegalStateException("record id should be empty for record: " + record);
        }
        if (record.getDtCreate() != null) {
            throw new IllegalStateException("record creation date should be empty for record: " + record);
        }
        if (record.getProductId() == null && record.getMealId() == null) {
            throw new IllegalStateException("record should contain product or meal: " + record);
        }
    }

    private void checkWeight(Record record) {
        if (record.getWeight() == null || record.getWeight() <= 0) {
            throw new IllegalArgumentException("weight is not valid for record:" + record);
        }
    }

    private void checkDtSupply(Record record) {
        if (record.getDtSupply() == null || record.getDtSupply().isAfter(Instant.now())) {
            throw new IllegalArgumentException("supply date is not valid for record:" + record);
        }
    }
}
