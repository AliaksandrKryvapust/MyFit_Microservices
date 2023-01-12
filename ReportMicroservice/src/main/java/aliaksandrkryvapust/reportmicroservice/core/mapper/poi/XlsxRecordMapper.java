package aliaksandrkryvapust.reportmicroservice.core.mapper.poi;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.RecordDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxIngredient;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class XlsxRecordMapper {
    private final XlsxIngredientMapper ingredientMapper;

    public XlsxRecordMapper(XlsxIngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public XlsxRecord inputMapping(RecordDto recordDtoInput) {
        if (recordDtoInput.getProduct()!=null) {
            return XlsxRecord.builder()
                    .recordWeight(recordDtoInput.getWeight())
                    .dtSupply(recordDtoInput.getDtSupply())
                    .productTitle(recordDtoInput.getProduct().getTitle())
                    .productCalories(recordDtoInput.getProduct().getCalories())
                    .productProteins(recordDtoInput.getProduct().getProteins())
                    .productFats(recordDtoInput.getProduct().getFats())
                    .productCarbohydrates(recordDtoInput.getProduct().getCarbohydrates())
                    .productWeight(recordDtoInput.getProduct().getWeight())
                    .composition(new ArrayList<>())
                    .build();
        } else {
            List<XlsxIngredient> ingredient = recordDtoInput.getRecipe().getComposition().stream()
                    .map(ingredientMapper::inputMapping).collect(Collectors.toList());
            return XlsxRecord.builder()
                    .recordWeight(recordDtoInput.getWeight())
                    .dtSupply(recordDtoInput.getDtSupply())
                    .mealTitle(recordDtoInput.getRecipe().getTitle())
                    .composition(ingredient)
                    .build();
        }
    }

    public List<XlsxRecord> listInputMapping(List<RecordDto> recordDtoInputs) {
        return recordDtoInputs.stream().map(this::inputMapping).collect(Collectors.toList());
    }
}
