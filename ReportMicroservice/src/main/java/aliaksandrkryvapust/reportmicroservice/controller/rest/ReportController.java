package aliaksandrkryvapust.reportmicroservice.controller.rest;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.FileDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportManager;
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
@RequestMapping("/api/v1/report")
public class ReportController {
    private final IReportManager reportManager;

    @GetMapping(params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput<ReportDtoOutput>> getPage(@RequestParam("page") int page,
                                                                     @RequestParam("size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dtCreate").descending());
        PageDtoOutput<ReportDtoOutput> dtoOutput = reportManager.getDto(pageable);
        return ResponseEntity.ok(dtoOutput);
    }

    @RequestMapping(value = "/{uuid}/export", method = RequestMethod.GET)
    public ResponseEntity<FileDtoOutput> export(@PathVariable UUID uuid) {
        FileDtoOutput dtoOutput = reportManager.getReportFile(uuid);
        return ResponseEntity.ok(dtoOutput);
    }

    @PostMapping("/{type}")
    protected ResponseEntity<Object> post(@PathVariable("type") EType type, @RequestBody @Valid ParamsDto paramsDto) {
        reportManager.saveDto(paramsDto, type);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
