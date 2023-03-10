package itacad.aliaksandrkryvapust.productmicroservice.service.api;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.RecordDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IRecordManager {
    RecordDtoOutput saveDto(RecordDtoInput type, UUID uuid_profile);
    PageDtoOutput<RecordDtoOutput> getDto(Pageable pageable, UUID uuid_profile);
    List<RecordDto> getRecord(ParamsDto paramsDto);
}
