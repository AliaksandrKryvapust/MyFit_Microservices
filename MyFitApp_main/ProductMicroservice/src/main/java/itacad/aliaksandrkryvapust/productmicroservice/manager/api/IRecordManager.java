package itacad.aliaksandrkryvapust.productmicroservice.manager.api;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.RecordDto;

import java.util.List;

public interface IRecordManager extends IManager<RecordDtoOutput, RecordDtoInput>{
    List<RecordDto> getRecordByTimeGap(ParamsDto paramsDto);
}
