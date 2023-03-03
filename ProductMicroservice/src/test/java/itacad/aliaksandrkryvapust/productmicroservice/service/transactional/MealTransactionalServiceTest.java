package itacad.aliaksandrkryvapust.productmicroservice.service.transactional;

import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IIngredientRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IMealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class MealTransactionalServiceTest {
    @InjectMocks
    private MealTransactionalService transactionalService;
    @Mock
    private IIngredientRepository ingredientRepository;
    @Mock
    private IMealRepository mealRepository;
    // preconditions
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");

    @Test
    void deleteTransactional() {
        // preconditions

        //test
        transactionalService.deleteTransactional(id, id);
        Mockito.verify(ingredientRepository, Mockito.times(1)).deleteAllByMealId(id);
        Mockito.verify(mealRepository, Mockito.times(1)).deleteByIdAndUserId(id, id);

        // assert
    }
}