package aliaksandrkryvapust.reportmicroservice.controller.rest;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.manager.api.IReportManager;
import lombok.RequiredArgsConstructor;
import org.apache.poi.util.IOUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        PageDtoOutput<ReportDtoOutput> dtoOutput = reportManager.get(pageable);
        return ResponseEntity.ok(dtoOutput);
    }

    @RequestMapping(value = "/{uuid}/export", method = RequestMethod.GET)
    public void export(@PathVariable UUID uuid, HttpServletResponse response) {
        byte[] output = reportManager.getReportFile(uuid);
        try (InputStream data = new ByteArrayInputStream(output)) {
            prepareHeadersAndOutputStream(uuid, response, data);
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException("Error during xlsx export");
        }
    }

    @PostMapping("/{type}")
    protected ResponseEntity<Object> post(@PathVariable("type") EType type, @RequestBody @Valid ParamsDto paramsDto) {
        reportManager.save(paramsDto, type);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void prepareHeadersAndOutputStream(UUID uuid, HttpServletResponse response, InputStream data) throws IOException {
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uuid.toString() + ".xlsx");
        IOUtils.copy(data, response.getOutputStream());
    }
}
