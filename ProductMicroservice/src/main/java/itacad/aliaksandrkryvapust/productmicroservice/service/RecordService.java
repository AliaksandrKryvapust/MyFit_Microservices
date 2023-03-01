package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IRecordRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IMealService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecordService implements IRecordService {
    private final IRecordRepository recordRepository;
    private final IProductService productService;
    private final IMealService mealService;

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
}
