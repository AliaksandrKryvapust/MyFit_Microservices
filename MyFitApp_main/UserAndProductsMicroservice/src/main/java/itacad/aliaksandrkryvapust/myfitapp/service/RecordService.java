package itacad.aliaksandrkryvapust.myfitapp.service;

import itacad.aliaksandrkryvapust.myfitapp.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.myfitapp.repository.api.IRecordRepository;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Meal;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Product;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IMealService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IProductService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecordService implements IRecordService {
    private final IRecordRepository recordRepository;
    private final IProductService productService;
    private final IMealService mealService;

    @Autowired
    public RecordService(IRecordRepository recordRepository, IProductService productService, IMealService mealService) {
        this.recordRepository = recordRepository;
        this.productService = productService;
        this.mealService = mealService;
    }

    @Override
    public Record save(Record record) {
        validateInput(record);
        setFieldsFromDatabase(record);
        return this.recordRepository.save(record);
    }

    private void setFieldsFromDatabase(Record record) {
        if (record.getProductId() != null) {
            Product product = this.productService.get(record.getProductId());
            record.setProduct(product);
        }
        if (record.getMealId() != null) {
            Meal meal = this.mealService.get(record.getMealId());
            record.setMeal(meal);
        }
    }

    private void validateInput(Record record) {
        if (record.getId() != null) {
            throw new IllegalStateException("Record id should be empty");
        }
    }

    @Override
    public Page<Record> get(Pageable pageable) {
        return this.recordRepository.findAll(pageable);
    }

    @Override
    public Record get(UUID id) {
        return this.recordRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Record> getRecordByTimeGap(ParamsDto paramsDto) {
        return this.recordRepository.getRecordByTimeGap(paramsDto.getFrom(), paramsDto.getTo());
    }
}
