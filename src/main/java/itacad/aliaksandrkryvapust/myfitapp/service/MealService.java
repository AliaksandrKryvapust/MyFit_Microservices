package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IMealRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Meal;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class MealService implements IMealService {
    private final IMealRepository mealRepository;
    @Autowired
    public MealService(IMealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal) {
        if (meal.getId() != null || meal.getDtUpdate() != null) {
            throw new IllegalStateException("Meal id & version should be empty");
        }
        return this.mealRepository.save(meal);
    }

    @Override
    public List<Meal> get() {
        return this.mealRepository.findAll();
    }

    @Override
    public Meal get(UUID id) {
        return this.mealRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
    this.mealRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Meal update(Meal meal, UUID id, Instant version) {
        if (meal.getId() != null || meal.getDtUpdate() != null) {
            throw new IllegalStateException("Meal id & version should be empty");
        }
        Meal currentEntity = this.mealRepository.findById(id).orElseThrow();
        if (!currentEntity.getDtUpdate().equals(version)) {
            throw new OptimisticLockException("meal table update failed, version does not match update denied");
        }
        return this.mealRepository.save(meal);
    }
}
