package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IRecordRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IMealService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecordService implements IRecordService {
    private final IRecordRepository recordRepository;
    private final IProductService productService;
    private final IMealService mealService;

    public RecordService(IRecordRepository recordRepository, IProductService productService, IMealService mealService) {
        this.recordRepository = recordRepository;
        this.productService = productService;
        this.mealService = mealService;
    }

    @Override
    public Record save(Record record) {
        validateInput(record);
        setFieldsFromDatabase(record); // TODO
        return this.recordRepository.save(record);
    }

    private void setFieldsFromDatabase(Record record) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (record.getProductId() != null) { // TODO add optional
            Product product = this.productService.get(record.getProductId(), userDetails.getId());
            record.setProduct(product);
        }
        if (record.getMealId() != null) {
            Meal meal = this.mealService.get(record.getMealId(), userDetails.getId());
            record.setMeal(meal);
        }
    }

    private void validateInput(Record record) {
        if (record.getId() != null) {
            throw new IllegalStateException("Record id should be empty");
        }
    }

    @Override
    public Page<Record> get(Pageable pageable, UUID userId) {
        return this.recordRepository.findAllByUserId(pageable, userId);
    }

    @Override
    public Record get(UUID id, UUID userId) {
        return this.recordRepository.findByIdAndUserId(id, userId).orElseThrow();
    }

    @Override
    public List<Record> getRecordByTimeGap(ParamsDto paramsDto) {
        return this.recordRepository.getRecordByTimeGap(paramsDto.getFrom(), paramsDto.getTo());
    }
}
