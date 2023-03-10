package aliaksandrkryvapust.reportmicroservice.service.validator;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Params;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IParamsValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class ParamsValidator implements IParamsValidator {
    @Override
    public void validateEntity(Params params) {
        checkStart(params);
        checkFinish(params);
    }

    private void checkStart(Params params) {
        if (params.getStart() == null) {
            throw new IllegalArgumentException("start date can not be null for params:" + params);
        }
        if (params.getStart().isAfter(params.getFinish())) {
            throw new IllegalArgumentException("start date must refer to moment in the past or present for params:" + params);
        }
    }

    private void checkFinish(Params params) {
        if (params.getFinish() == null) {
            throw new IllegalArgumentException("start date can not be null for params:" + params);
        }
        if (params.getFinish().isAfter(LocalDate.now(ZoneOffset.UTC).plusDays(1)) ||
                params.getFinish().isBefore(params.getStart())) {
            throw new IllegalArgumentException("start date must refer to moment in the past or present for params:" + params);
        }
    }
}
