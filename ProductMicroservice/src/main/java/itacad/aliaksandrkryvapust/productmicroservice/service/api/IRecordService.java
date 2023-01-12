package itacad.aliaksandrkryvapust.productmicroservice.service.api;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;

import java.util.List;
import java.util.UUID;

public interface IRecordService extends IService<Record>{
    List<Record> getRecordByTimeGap(ParamsDto paramsDto);
}
