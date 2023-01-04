package aliaksandrkryvapust.reportmicroservice.core.mapper.poi;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.RecordDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxMeal;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxProduct;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class XlsxRecordMapper {
    private final XlsxProductMapper productMapper;
    private final XlsxMealMapper mealMapper;

    @Autowired
    public XlsxRecordMapper(XlsxProductMapper productMapper, XlsxMealMapper mealMapper) {
        this.productMapper = productMapper;
        this.mealMapper = mealMapper;
    }

    public XlsxRecord inputMapping(RecordDto recordDtoInput) {
        if (recordDtoInput.getProduct() == null) {
            XlsxMeal meal = mealMapper.inputMapping(recordDtoInput.getRecipe());
            return XlsxRecord.builder()
                    .weight(recordDtoInput.getWeight())
                    .dtSupply(recordDtoInput.getDtSupply())
                    .recipe(meal)
                    .build();
        } else {
            XlsxProduct product = productMapper.inputMapping(recordDtoInput.getProduct());
            return XlsxRecord.builder()
                    .weight(recordDtoInput.getWeight())
                    .dtSupply(recordDtoInput.getDtSupply())
                    .product(product)
                    .build();
        }
    }

    public List<XlsxRecord> listInputMapping(List<RecordDto> recordDtoInputs) {
        return recordDtoInputs.stream().map(this::inputMapping).collect(Collectors.toList());
    }
}
