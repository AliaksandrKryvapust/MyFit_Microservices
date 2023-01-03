package itacad.aliaksandrkryvapust.myfitapp.service.api;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;

import java.time.Instant;
import java.util.List;

public interface IRecordService extends IService<Record>{
    List<Record> getRecordByTimeGap(ParamsDto paramsDto);
}
