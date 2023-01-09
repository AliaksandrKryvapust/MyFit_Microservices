package itacad.aliaksandrkryvapust.productmicroservice.manager.api;


import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;

public interface IMealManager extends IManager<MealDtoOutput, MealDtoInput>,
        IManagerUpdate<MealDtoOutput, MealDtoInput>, IManagerDelete {
}
