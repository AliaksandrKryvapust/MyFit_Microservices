package itacad.aliaksandrkryvapust.myfitapp.manager.api;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.MealDtoOutput;

public interface IMealManager extends IManager<MealDtoOutput, MealDtoInput>,
        IManagerUpdate<MealDtoOutput, MealDtoInput>, IManagerDelete {
}
