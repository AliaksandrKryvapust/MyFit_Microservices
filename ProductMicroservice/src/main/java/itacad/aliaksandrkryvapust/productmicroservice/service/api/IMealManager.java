package itacad.aliaksandrkryvapust.productmicroservice.service.api;


import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IManagerDelete;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IManagerUpdate;

public interface IMealManager extends IManager<MealDtoOutput, MealDtoInput>,
        IManagerUpdate<MealDtoOutput, MealDtoInput>, IManagerDelete {
}
