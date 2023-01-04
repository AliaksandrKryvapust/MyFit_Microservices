package aliaksandrkryvapust.reportmicroservice.core.mapper.poi;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.MealDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxIngredient;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxMeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class XlsxMealMapper {
    private final XlsxIngredientMapper ingredientMapper;

    @Autowired
    public XlsxMealMapper(XlsxIngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public XlsxMeal inputMapping(MealDto mealDto) {
        List<XlsxIngredient> ingredient = mealDto.getComposition().stream().map(ingredientMapper::inputMapping)
                .collect(Collectors.toList());
        return XlsxMeal.builder().composition(ingredient)
                .title(mealDto.getTitle())
                .build();
    }
}
