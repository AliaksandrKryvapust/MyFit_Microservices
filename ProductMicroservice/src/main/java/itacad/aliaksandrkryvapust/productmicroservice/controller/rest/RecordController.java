package itacad.aliaksandrkryvapust.productmicroservice.controller.rest;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.RecordDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.ParamsMapper;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IRecordManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RecordController {
    private final IRecordManager recordManager;
    private final ParamsMapper paramsMapper;

    @GetMapping(path = "profile/{uuid_profile}/journal/food", params = {"page", "size"})
    protected ResponseEntity<PageDtoOutput<RecordDtoOutput>> getPage(@RequestParam("page") int page,
                                                                     @RequestParam("size") int size,
                                                                     @PathVariable(name = "uuid_profile") UUID uuidProfile) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dtCreate").descending());
        PageDtoOutput<RecordDtoOutput> dtoOutput = recordManager.get(pageable, uuidProfile);
        return ResponseEntity.ok(dtoOutput);
    }

    @PostMapping("profile/{uuid_profile}/journal/food")
    protected ResponseEntity<RecordDtoOutput> post(@RequestBody @Valid RecordDtoInput dtoInput,
                                                   @PathVariable (name = "uuid_profile") UUID uuidProfile) {
        RecordDtoOutput dtoOutput = recordManager.save(dtoInput, uuidProfile);
        return new ResponseEntity<>(dtoOutput, HttpStatus.CREATED);
    }

    @GetMapping("/journal/food/export")
    protected ResponseEntity<List<RecordDto>> export(HttpServletRequest request) {
        ParamsDto paramsDto = paramsMapper.getParamsDto(request);
        List<RecordDto> dtoList = recordManager.getRecordByTimeGap(paramsDto);
        return ResponseEntity.ok(dtoList);
    }
}
