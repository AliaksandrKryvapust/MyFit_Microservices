package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.IngredientDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.IngredientDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.IngredientMapper;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Ingredient;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IIngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//@Component
//public class IngredientManager implements IIngredientManager {
//    private final IngredientMapper ingredientMapper;
//    private final IIngredientService ingredientService;
//
//    @Autowired
//    public IngredientManager(IngredientMapper ingredientMapper, IIngredientService ingredientService) {
//        this.ingredientMapper = ingredientMapper;
//        this.ingredientService = ingredientService;
//    }
//
//    @Override
//    public IngredientDtoOutput save(IngredientDtoInput dtoInput) {
//        Ingredient ingredient = this.ingredientService.save(ingredientMapper.inputMapping(dtoInput));
//        return ingredientMapper.outputMapping(ingredient);
//    }
//
//    @Override
//    public List<IngredientDtoOutput> get(Pageable pageable) {
//        return this.ingredientService.get(pageable).stream().map(ingredientMapper::outputMapping).collect(Collectors.toList());
//    }
//
//    @Override
//    public IngredientDtoOutput get(UUID id) {
//        return ingredientMapper.outputMapping(this.ingredientService.get(id));
//    }
//
//    @Override
//    public void delete(UUID id) {
//        this.ingredientService.delete(id);
//    }
//
//    @Override
//    public IngredientDtoOutput update(IngredientDtoInput dtoInput, UUID id, Long version) {
//        Ingredient ingredient = this.ingredientService.update(ingredientMapper.inputMapping(dtoInput), id, version);
//        return ingredientMapper.outputMapping(ingredient);
//    }
//}
