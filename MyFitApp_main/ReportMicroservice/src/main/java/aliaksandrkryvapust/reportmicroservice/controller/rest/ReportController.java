package aliaksandrkryvapust.reportmicroservice.controller.rest;

import aliaksandrkryvapust.reportmicroservice.core.dto.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.manager.api.IReportManager;
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

    @GetMapping("/{uuid}")
    protected ResponseEntity<ReportDtoOutput> get(@PathVariable UUID uuid) {
        return ResponseEntity.ok(reportManager.get(uuid));
    }

    @PostMapping
    protected ResponseEntity<Object> post(@RequestBody ParamsDto paramsDto) {
        this.reportManager.save(paramsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
