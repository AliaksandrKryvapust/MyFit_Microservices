package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.MealMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IMealManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Meal;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MealManager implements IMealManager {
    private final MealMapper mealMapper;
    private final IMealService mealService;

    @Autowired
    public MealManager(MealMapper mealMapper, IMealService mealService) {
        this.mealMapper = mealMapper;
        this.mealService = mealService;
    }

    @Override
    public MealDtoOutput save(MealDtoInput dtoInput) {
        Meal meal = this.mealService.save(mealMapper.inputMapping(dtoInput));
        return mealMapper.outputMapping(meal);
    }

    @Override
    public List<MealDtoOutput> get() {
        return this.mealService.get().stream().map(mealMapper::outputMapping).collect(Collectors.toList());
    }

    @Override
    public MealDtoOutput get(UUID id) {
        return mealMapper.outputMapping(this.mealService.get(id));
    }

    @Override
    public void delete(UUID id) {
        this.mealService.delete(id);
    }

    @Override
    public MealDtoOutput update(MealDtoInput dtoInput, UUID id, Instant version) {
        Meal meal = this.mealService.update(mealMapper.inputMapping(dtoInput), id, version);
        return mealMapper.outputMapping(meal);
    }
}
