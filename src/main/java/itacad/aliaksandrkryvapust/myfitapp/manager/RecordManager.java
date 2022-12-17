package itacad.aliaksandrkryvapust.myfitapp.manager;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.RecordMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IRecordManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public List<RecordDtoOutput> get() {
        return this.recordService.get().stream().map(recordMapper::outputMapping).collect(Collectors.toList());
    }

    @Override
    public RecordDtoOutput get(UUID id) {
        return recordMapper.outputMapping(this.recordService.get(id));
    }
}
