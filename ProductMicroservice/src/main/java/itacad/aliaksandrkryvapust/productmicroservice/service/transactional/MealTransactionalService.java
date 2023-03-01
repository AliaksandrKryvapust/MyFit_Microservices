package itacad.aliaksandrkryvapust.productmicroservice.service.transactional;

import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IIngredientRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IMealRepository;
import itacad.aliaksandrkryvapust.productmicroservice.service.transactional.api.IMealTransactionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MealTransactionalService implements IMealTransactionalService {
    private final IIngredientRepository ingredientRepository;
    private final IMealRepository mealRepository;

    @Override
    public void deleteTransactional(UUID id, UUID userId) {
        ingredientRepository.deleteAllByMealId(id);
        mealRepository.deleteByIdAndUserId(id, userId);
    }
}
