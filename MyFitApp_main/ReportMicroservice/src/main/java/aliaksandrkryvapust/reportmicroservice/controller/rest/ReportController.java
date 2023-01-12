package aliaksandrkryvapust.reportmicroservice.controller.rest;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.manager.api.IReportManager;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EType;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @RequestMapping(value = "/{uuid}/export", method = RequestMethod.GET)
    public void export(@PathVariable UUID uuid, HttpServletResponse response) {
        byte[] output = reportManager.getReportFile(uuid);
        try (InputStream data = new ByteArrayInputStream(output)) {
            this.prepareHeadersAndOutputStream(uuid, response, data);
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException("Error during xlsx export");
        }
    }

    private void prepareHeadersAndOutputStream(UUID uuid, HttpServletResponse response, InputStream data) throws IOException {
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + uuid.toString() + ".xlsx");
        IOUtils.copy(data, response.getOutputStream());
    }

    @PostMapping("/{type}")
    protected ResponseEntity<Object> post(@PathVariable("type") EType type, @RequestBody @Valid ParamsDto paramsDto) {
        this.reportManager.save(paramsDto, type);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
