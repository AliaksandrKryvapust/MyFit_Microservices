package itacad.aliaksandrkryvapust.myfitapp.core.mapper;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.IngredientDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MealMapper {
    private final IngredientMapper ingredientMapper;

    @Autowired
    public MealMapper(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public Meal inputMapping(MealDtoInput mealDtoInput) {
        List<Ingredient> ingredient = mealDtoInput.getIngredients().stream().map(ingredientMapper::inputMapping)
                .collect(Collectors.toList());
        return Meal.builder().ingredients(ingredient)
                .title(mealDtoInput.getTitle())
                .build();
    }

    public MealDtoOutput outputMapping(Meal meal) {
        List<IngredientDtoOutput> ingredients = meal.getIngredients().stream().map(ingredientMapper::outputMapping)
                .collect(Collectors.toList());
        return MealDtoOutput.builder()
                .id(meal.getId())
                .ingredients(ingredients)
                .title(meal.getTitle())
                .dtCreate(meal.getDtCreate())
                .dtUpdate(meal.getDtUpdate())
                .build();
    }
}
