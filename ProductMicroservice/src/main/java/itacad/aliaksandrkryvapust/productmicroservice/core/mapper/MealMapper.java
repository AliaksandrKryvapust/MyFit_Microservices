package itacad.aliaksandrkryvapust.productmicroservice.core.mapper;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.IngredientDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
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

    public Meal inputMapping(MealDtoInput mealDtoInput, MyUserDetails userDetails) {
        List<Ingredient> ingredient = mealDtoInput.getComposition().stream().map(ingredientMapper::inputMapping)
                .collect(Collectors.toList());
        return Meal.builder().ingredients(ingredient)
                .title(mealDtoInput.getTitle())
                .userId(userDetails.getId())
                .build();
    }

    public MealDtoOutput outputMapping(Meal meal) {
        List<IngredientDtoOutput> ingredients = meal.getIngredients().stream().map(ingredientMapper::outputMapping)
                .collect(Collectors.toList());
        return MealDtoOutput.builder()
                .uuid(meal.getId())
                .composition(ingredients)
                .title(meal.getTitle())
                .dtCreate(meal.getDtCreate())
                .dtUpdate(meal.getDtUpdate())
                .build();
    }

    public PageDtoOutput<MealDtoOutput> outputPageMapping(Page<Meal> meal) {
        List<MealDtoOutput> outputs = meal.getContent().stream().map(this::outputMapping).collect(Collectors.toList());
        return PageDtoOutput.<MealDtoOutput>builder()
                .number(meal.getNumber()+1)
                .size(meal.getSize())
                .totalPages(meal.getTotalPages())
                .totalElements(meal.getTotalElements())
                .first(meal.isFirst())
                .numberOfElements(meal.getNumberOfElements())
                .last(meal.isLast())
                .content(outputs)
                .build();
    }
}
