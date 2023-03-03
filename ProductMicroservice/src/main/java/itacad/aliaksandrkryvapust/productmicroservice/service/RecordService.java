package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.RecordDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.RecordMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IRecordRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.*;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.RecordValidator;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IRecordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecordService implements IRecordService, IRecordManager {
    private final static String RECORD_POST = "New record was created";
    private final IRecordRepository recordRepository;
    private final IProductService productService;
    private final IMealService mealService;
    private final RecordMapper recordMapper;
    private final IRecordValidator recordValidator;
    private final IProfileService profileService;
    private final AuditMapper auditMapper;
    private final IAuditManager auditManager;

    @Override
    public Record save(Record record) {
        setFieldsFromDatabase(record);
        return recordRepository.save(record);
    }

    @Override
    public Page<Record> get(Pageable pageable, UUID userId) {
        return recordRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public Record get(UUID id, UUID userId) {
        return recordRepository.findByIdAndUserId(id, userId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Record> getRecordByTimeGap(ParamsDto paramsDto) {
        return recordRepository.getRecordByTimeGap(paramsDto.getFrom(), paramsDto.getTo(),
                UUID.fromString(paramsDto.getUserId()));
    }

    private void setFieldsFromDatabase(Record record) {
        if (record.getProductId() != null) {
            Product product = productService.get(record.getProductId(), record.getUserId());
            record.setProduct(product);
        }
        if (record.getMealId() != null) {
            Meal meal = mealService.get(record.getMealId(), record.getUserId());
            record.setMeal(meal);
        }
    }

    @Override
    public RecordDtoOutput saveDto(RecordDtoInput dtoInput, UUID uuid_profile) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        profileService.get(uuid_profile, userDetails.getId());
        Record entityToSave = recordMapper.inputMapping(dtoInput, userDetails);
        recordValidator.validateEntity(entityToSave);
        Record record = save(entityToSave);
        prepareAudit(record, userDetails, RECORD_POST);
        return recordMapper.outputMapping(record);
    }

    @Override
    public PageDtoOutput<RecordDtoOutput> getDto(Pageable pageable, UUID uuid_profile) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        profileService.get(uuid_profile, userDetails.getId());
        Page<Record> page = get(pageable, userDetails.getId());
        return recordMapper.outputPageMapping(page);
    }

    @Override
    public List<RecordDto> getRecord(ParamsDto paramsDto) {
        List<Record> records = getRecordByTimeGap(paramsDto);
        return recordMapper.listOutputMapping(records);
    }

    private void prepareAudit(Record record, MyUserDetails userDetails, String method) {
        AuditDto auditDto = auditMapper.recordOutputMapping(record, userDetails, method);
        auditManager.audit(auditDto);
    }
}
