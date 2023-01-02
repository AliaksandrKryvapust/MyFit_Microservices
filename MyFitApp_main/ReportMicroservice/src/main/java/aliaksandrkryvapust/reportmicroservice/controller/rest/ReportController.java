package aliaksandrkryvapust.reportmicroservice.controller.rest;

import aliaksandrkryvapust.reportmicroservice.core.dto.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.manager.api.IReportManager;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/report")
public class ReportController {
    private final IReportManager reportManager;

    @Autowired
    public ReportController(IReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput> getPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(reportManager.get(pageable));
    }

    @GetMapping("/{uuid}/export")
    protected ResponseEntity<ReportDtoOutput> get(@PathVariable UUID uuid) {
        return ResponseEntity.ok(reportManager.get(uuid));
    }

    @PostMapping("/{type}")
    protected ResponseEntity<ReportDtoOutput> post(@PathVariable("type") Type type, @RequestBody @Valid ParamsDto paramsDto) {
        ReportDtoOutput dtoOutput = this.reportManager.save(paramsDto, type);
        return new ResponseEntity<>(dtoOutput, HttpStatus.CREATED);
    }
}
