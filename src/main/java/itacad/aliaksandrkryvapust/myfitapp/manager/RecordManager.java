package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.RecordMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IRecordManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RecordManager implements IRecordManager {
    private final RecordMapper recordMapper;
    private final IRecordService recordService;

    @Autowired
    public RecordManager(RecordMapper recordMapper, IRecordService recordService) {
        this.recordMapper = recordMapper;
        this.recordService = recordService;
    }

    @Override
    public RecordDtoOutput save(RecordDtoInput dtoInput) {
        Record record = this.recordService.save(recordMapper.inputMapping(dtoInput));
        return recordMapper.outputMapping(record);
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        return recordMapper.outputPageMapping(this.recordService.get(pageable));
    }

    @Override
    public RecordDtoOutput get(UUID id) {
        return recordMapper.outputMapping(this.recordService.get(id));
    }
}
