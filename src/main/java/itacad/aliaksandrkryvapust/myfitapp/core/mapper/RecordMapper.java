package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RecordMapper {
    private final ProductMapper productMapper;
    private final MealMapper mealMapper;

    @Autowired
    public RecordMapper(ProductMapper productMapper, MealMapper mealMapper) {
        this.productMapper = productMapper;
        this.mealMapper = mealMapper;
    }

    public Record inputMapping(RecordDtoInput recordDtoInput) {
        return Record.builder().productId(recordDtoInput.getProductId())
                .mealId(recordDtoInput.getMealId())
                .weight(recordDtoInput.getWeight())
                .build();
    }

    public RecordDtoOutput outputMapping(Record record) {
        RecordDtoOutput recordDtoOutput = RecordDtoOutput.builder()
                .uuid(record.getId())
                .weight(record.getWeight())
                .dtCreate(record.getDtCreate())
                .build();
        if (record.getProduct() != null) {
            ProductDtoOutput productDtoOutput = this.productMapper.outputMapping(record.getProduct());
            recordDtoOutput.setProduct(productDtoOutput);
        }
        if (record.getMeal() != null) {
            MealDtoOutput mealDtoOutput = this.mealMapper.outputMapping(record.getMeal());
            recordDtoOutput.setRecipe(mealDtoOutput);
        }
        return recordDtoOutput;
    }

    public PageDtoOutput<RecordDtoOutput> outputPageMapping(Page<Record> record) {
        List<RecordDtoOutput> outputs = record.getContent().stream().map(this::outputMapping).collect(Collectors.toList());
        return PageDtoOutput.<RecordDtoOutput>builder()
                .number(record.getNumber()+1)
                .size(record.getSize())
                .totalPages(record.getTotalPages())
                .totalElements(record.getTotalElements())
                .first(record.isFirst())
                .numberOfElements(record.getNumberOfElements())
                .last(record.isLast())
                .content(outputs)
                .build();
    }
}
