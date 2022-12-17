package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.repository.api.IRecordRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecordService implements IRecordService {
    private final IRecordRepository recordRepository;

    public RecordService(IRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public Record save(Record record) {
        if (record.getId() != null) {
            throw new IllegalStateException("Record id should be empty");
        }
        return this.recordRepository.save(record);
    }

    @Override
    public List<Record> get() {
        return this.recordRepository.findAll();
    }

    @Override
    public Record get(UUID id) {
        return this.recordRepository.findById(id).orElseThrow();
    }
}
