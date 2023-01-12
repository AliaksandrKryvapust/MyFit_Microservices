package itacad.aliaksandrkryvapust.productmicroservice.service.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Ingredient;

import java.util.UUID;

public interface IIngredientService extends IService<Ingredient>, IServiceUpdate<Ingredient>, IServiceDelete {
    void deleteAllByMealId(UUID uuid);
}
