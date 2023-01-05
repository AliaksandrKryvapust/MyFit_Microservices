package itacad.aliaksandrkryvapust.myfitapp.manager.api;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.microservices.RecordDto;

import java.time.Instant;
import java.util.List;

public interface IRecordManager extends IManager<RecordDtoOutput, RecordDtoInput>{
    List<RecordDto> getRecordByTimeGap(ParamsDto paramsDto);
}
