package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.ProductDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IProductManager;
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
    protected ResponseEntity<ProductDtoOutput> post(@RequestBody @Valid ProductDtoInput dtoInput, HttpServletRequest request) {
        return new ResponseEntity<>(this.productManager.save(dtoInput, request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/dt_update/{version}")
    protected ResponseEntity<ProductDtoOutput> put(@PathVariable UUID id, @PathVariable(name = "version")
    String version, @Valid @RequestBody ProductDtoInput dtoInput, HttpServletRequest request) {
        return ResponseEntity.ok(this.productManager.update(dtoInput, id, Long.parseLong(version), request));
    }

    @DeleteMapping("/{id}")
    protected ResponseEntity<Object> delete(@PathVariable UUID id, HttpServletRequest request) {
        productManager.delete(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}