package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RecordMapper {
    public Record inputMapping(RecordDtoInput recordDtoInput) {
        return Record.builder().productId(recordDtoInput.getProductId())
                .mealId(recordDtoInput.getMealId())
                .weight(recordDtoInput.getWeight())
                .build();
    }

    public RecordDtoOutput outputMapping(Record record) {
        return RecordDtoOutput.builder()
                .id(record.getId())
                .productId(record.getProductId())
                .mealId(record.getMealId())
                .weight(record.getWeight())
                .dtCreate(record.getDtCreate())
                .build();
    }
}
