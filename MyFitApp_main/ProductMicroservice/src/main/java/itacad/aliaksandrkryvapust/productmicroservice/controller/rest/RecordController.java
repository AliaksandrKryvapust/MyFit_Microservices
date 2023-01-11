package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.RecordDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IRecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/v1")
public class RecordController {
    private final IRecordManager recordManager;

    @Autowired
    public RecordController(IRecordManager recordManager) {
        this.recordManager = recordManager;
    }

    @GetMapping(path = "profile/{uuid_profile}/journal/food",params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput> getPage(@RequestParam("page") int page,
                                                    @RequestParam("size") int size,
                                                    @PathVariable (name = "uuid_profile") UUID uuidProfile) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(recordManager.get(pageable, uuidProfile));
    }

    @PostMapping("profile/{uuid_profile}/journal/food")
    protected ResponseEntity<RecordDtoOutput> post(@RequestBody @Valid RecordDtoInput dtoInput,
                                                   @PathVariable (name = "uuid_profile") UUID uuidProfile) {
        return new ResponseEntity<>(this.recordManager.save(dtoInput, uuidProfile), HttpStatus.CREATED);
    }

    @GetMapping("/journal/food/export")
    protected ResponseEntity<List<RecordDto>> export(HttpServletRequest request) {
        ParamsDto paramsDto = getParamsDto(request);
        return ResponseEntity.ok(recordManager.getRecordByTimeGap(paramsDto));
    }

    private ParamsDto getParamsDto(HttpServletRequest request) {
        final LocalDate dateFrom = LocalDate.parse(request.getHeader("from"));
        final LocalDate dateTo = LocalDate.parse(request.getHeader("to"));
        return ParamsDto.builder().from(dateFrom.atStartOfDay().toInstant(ZoneOffset.UTC))
                .to(dateTo.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC)).build();
    }
}
