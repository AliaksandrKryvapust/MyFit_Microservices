package itacad.aliaksandrkryvapust.myfitapp.controller.rest;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IRecordManager;
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
@RequestMapping("api/journal/food")
public class RecordController {
    private final IRecordManager recordManager;

    @Autowired
    public RecordController(IRecordManager recordManager) {
        this.recordManager = recordManager;
    }

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput> getPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(recordManager.get(pageable));
    }

    @GetMapping("/{id}")
    protected ResponseEntity<RecordDtoOutput> get(@PathVariable UUID id) {
        return ResponseEntity.ok(recordManager.get(id));
    }

    @PostMapping
    protected ResponseEntity<RecordDtoOutput> post(@RequestBody @Valid RecordDtoInput dtoInput) {
        return new ResponseEntity<>(this.recordManager.save(dtoInput), HttpStatus.CREATED);
    }

}
