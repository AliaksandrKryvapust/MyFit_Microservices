package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IProductManager;
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
@RequestMapping("/api/v1/product")
public class ProductController {
    private final IProductManager productManager;

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput<ProductDtoOutput>> getPage(@RequestParam("page") int page,
                                                                      @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dtCreate").descending());
        PageDtoOutput<ProductDtoOutput> dtoOutput = productManager.get(pageable);
        return ResponseEntity.ok(dtoOutput);
    }

    @GetMapping("/{id}")
    protected ResponseEntity<ProductDtoOutput> get(@PathVariable UUID id) {
        ProductDtoOutput dtoOutput = productManager.get(id);
        return ResponseEntity.ok(dtoOutput);
    }

    @PostMapping
    protected ResponseEntity<ProductDtoOutput> post(@RequestBody @Valid ProductDtoInput dtoInput) {
        ProductDtoOutput dtoOutput = productManager.save(dtoInput);
        return new ResponseEntity<>(dtoOutput, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/version/{version}")
    protected ResponseEntity<ProductDtoOutput> put(@PathVariable UUID id, @PathVariable(name = "version")
    String version, @Valid @RequestBody ProductDtoInput dtoInput) {
        ProductDtoOutput dtoOutput = productManager.update(dtoInput, id, Long.parseLong(version));
        return ResponseEntity.ok(dtoOutput);
    }

    @DeleteMapping("/{id}")
    protected ResponseEntity<Object> delete(@PathVariable UUID id) {
        productManager.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
