package itacad.aliaksandrkryvapust.myfitapp.service.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Ingredient;

import java.util.UUID;

public interface IIngredientService extends IService<Ingredient>, IServiceUpdate<Ingredient>, IServiceDelete {
    void deleteAllByMealId(UUID uuid);
}
