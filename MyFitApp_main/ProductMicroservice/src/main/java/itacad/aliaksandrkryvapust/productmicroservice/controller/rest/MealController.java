package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IMealManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1/recipe")
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
    protected ResponseEntity<MealDtoOutput> post(@RequestBody @Valid MealDtoInput dtoInput, HttpServletRequest request) {
        return new ResponseEntity<>(this.mealManager.save(dtoInput, request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/dt_update/{version}")
    protected ResponseEntity<MealDtoOutput> put(@PathVariable UUID id, @PathVariable(name = "version") String version,
                                                @Valid @RequestBody MealDtoInput dtoInput, HttpServletRequest request) {
        return ResponseEntity.ok(this.mealManager.update(dtoInput, id, Long.valueOf(version), request));
    }

    @DeleteMapping("/{id}")
    protected ResponseEntity<Object> delete(@PathVariable UUID id, HttpServletRequest request) {
        mealManager.delete(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
