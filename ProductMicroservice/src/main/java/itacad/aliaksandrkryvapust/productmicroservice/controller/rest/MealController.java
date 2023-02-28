package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IMealManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
public class MealController {
    private final IMealManager mealManager;

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput<MealDtoOutput>> getPage(@RequestParam("page") int page,
                                                                   @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dtCreate").descending());
        PageDtoOutput<MealDtoOutput> dtoOutput = mealManager.get(pageable);
        return ResponseEntity.ok(dtoOutput);
    }

    @GetMapping("/{id}")
    protected ResponseEntity<MealDtoOutput> get(@PathVariable UUID id) {
        MealDtoOutput dtoOutput = mealManager.get(id);
        return ResponseEntity.ok(dtoOutput);
    }

    @PostMapping
    protected ResponseEntity<MealDtoOutput> post(@RequestBody @Valid MealDtoInput dtoInput) {
        MealDtoOutput dtoOutput = mealManager.save(dtoInput);
        return new ResponseEntity<>(dtoOutput, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/version/{version}")
    protected ResponseEntity<MealDtoOutput> put(@PathVariable UUID id, @PathVariable(name = "version") String version,
                                                @Valid @RequestBody MealDtoInput dtoInput) {
        MealDtoOutput dtoOutput = mealManager.update(dtoInput, id, Long.valueOf(version));
        return ResponseEntity.ok(dtoOutput);
    }

    @DeleteMapping("/{id}")
    protected ResponseEntity<Object> delete(@PathVariable UUID id) {
        mealManager.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
