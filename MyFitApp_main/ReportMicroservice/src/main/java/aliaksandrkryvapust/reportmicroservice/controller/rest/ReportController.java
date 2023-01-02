package aliaksandrkryvapust.reportmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.manager.api.IAuditManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit")
public class ReportController {
    private final IAuditManager auditManager;

    @Autowired
    public ReportController(IAuditManager auditManager) {
        this.auditManager = auditManager;
    }

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput> getPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(auditManager.get(pageable));
    }

    @GetMapping("/{uuid}")
    protected ResponseEntity<AuditDto> get(@PathVariable UUID uuid) {
        return ResponseEntity.ok(auditManager.get(uuid));
    }

    @PostMapping
    protected ResponseEntity<Object> post(@RequestBody AuditDto auditDto) {
        this.auditManager.save(auditDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
