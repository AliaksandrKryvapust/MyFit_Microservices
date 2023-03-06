package itacad.aliaksandrkryvapust.auditmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input.AuditDto;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.output.AuditDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.auditmicroservice.service.api.IAuditManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/audit")
public class AuditController {
    private final IAuditManager auditManager;

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput<AuditDtoOutput>> getPage(@RequestParam("page") int page,
                                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dtCreate").descending());
        PageDtoOutput<AuditDtoOutput> dtoOutput = auditManager.getDto(pageable);
        return ResponseEntity.ok(dtoOutput);
    }

    @GetMapping("/{uuid}")
    protected ResponseEntity<List<AuditDtoOutput>> get(@PathVariable UUID uuid) {
        List<AuditDtoOutput> dtoOutputs = auditManager.getActionDto(uuid);
        return ResponseEntity.ok(dtoOutputs);
    }

    @PostMapping
    protected ResponseEntity<Object> post(@RequestBody @Valid AuditDto auditDto) {
        auditManager.saveDto(auditDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
