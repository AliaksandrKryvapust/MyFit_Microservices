package itacad.aliaksandrkryvapust.auditmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.manager.api.IAuditManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {
    private final IAuditManager auditManager;

    @Autowired
    public AuditController(IAuditManager auditManager) {
        this.auditManager = auditManager;
    }

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput> getPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(auditManager.get(pageable));
    }

    @GetMapping("/{uuid}")
    protected ResponseEntity<List<AuditDtoOutput>> get(@PathVariable UUID uuid) {
        return ResponseEntity.ok(auditManager.getByRecord(uuid));
    }

    @PostMapping
    protected ResponseEntity<Object> post(@RequestBody @Valid AuditDto auditDto) {
        this.auditManager.save(auditDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
