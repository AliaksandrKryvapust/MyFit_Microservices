package itacad.aliaksandrkryvapust.myfitapp.controller.rest;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IProductManager;
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
@RequestMapping("/api/v1/product")
public class ProductController {
    private final IProductManager productManager;

    @Autowired
    public ProductController(IProductManager productManager) {
        this.productManager = productManager;
    }

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput> getPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(this.productManager.get(pageable));
    }

    @GetMapping("/{id}")
    protected ResponseEntity<ProductDtoOutput> get(@PathVariable UUID id) {
        return ResponseEntity.ok(productManager.get(id));
    }

    @PostMapping
    protected ResponseEntity<ProductDtoOutput> post(@RequestBody @Valid ProductDtoInput dtoInput) {
        return new ResponseEntity<>(this.productManager.save(dtoInput), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/dt_update/{version}")
    protected ResponseEntity<ProductDtoOutput> put(@PathVariable UUID id, @PathVariable(name = "version")
    String version, @Valid @RequestBody ProductDtoInput dtoInput) {
        return ResponseEntity.ok(this.productManager.update(dtoInput, id, Long.parseLong(version)));
    }

    @DeleteMapping("/{id}")
    protected ResponseEntity<Object> delete(@PathVariable UUID id) {
        productManager.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
