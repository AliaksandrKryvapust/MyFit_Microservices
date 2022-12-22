package itacad.aliaksandrkryvapust.myfitapp.controller.rest;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IMealManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("api/recipe")
public class MealController {
    private final IMealManager mealManager;

    @Autowired
    public MealController(IMealManager mealManager) {
        this.mealManager = mealManager;
    }

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput> getPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(mealManager.get(pageable));
    }

    @GetMapping("/{id}")
    protected ResponseEntity<MealDtoOutput> get(@PathVariable UUID id) {
        return ResponseEntity.ok(mealManager.get(id));
    }

    @PostMapping
    protected ResponseEntity<MealDtoOutput> post(@RequestBody @Valid MealDtoInput dtoInput) {
        return new ResponseEntity<>(this.mealManager.save(dtoInput), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/version/{version}")
    protected ResponseEntity<MealDtoOutput> put(@PathVariable UUID id, @PathVariable(name = "version") String version,
                                                @Valid @RequestBody MealDtoInput dtoInput) {
        return ResponseEntity.ok(this.mealManager.update(dtoInput, id, Long.valueOf(version)));
    }

    @DeleteMapping("/{id}")
    protected ResponseEntity<Object> delete(@PathVariable UUID id) {
        mealManager.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
