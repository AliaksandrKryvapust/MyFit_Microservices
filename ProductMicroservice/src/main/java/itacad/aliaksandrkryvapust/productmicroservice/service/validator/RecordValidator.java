package itacad.aliaksandrkryvapust.productmicroservice.service.validator;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IMealValidator;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IProductValidator;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IRecordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RecordValidator implements IRecordValidator {
    private final IProductValidator productValidator;
    private final IMealValidator mealValidator;

    @Override
    public void validateEntity(Record record) {
        checkAuxiliaryFields(record);
        checkProduct(record);
        checkMeal(record);
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
        if (record.getProduct() == null && record.getMeal() == null) {
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

    private void checkMeal(Record record) {
        if (record.getMeal() != null) {
            mealValidator.validateEntity(record.getMeal());
        }
    }

    private void checkProduct(Record record) {
        if (record.getProduct() != null) {
            productValidator.validateEntity(record.getProduct());
        }
    }
}
