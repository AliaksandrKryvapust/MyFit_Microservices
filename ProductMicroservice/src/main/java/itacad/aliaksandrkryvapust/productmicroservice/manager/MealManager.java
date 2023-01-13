package itacad.aliaksandrkryvapust.productmicroservice.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.MealMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.manager.api.IMealManager;
import itacad.aliaksandrkryvapust.productmicroservice.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Meal;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class MealManager implements IMealManager {
    private final static String mealPost = "New meal was created";
    private final static String mealPut = "Meal was updated";
    private final static String mealDelete = "Meal was deleted";
    private final MealMapper mealMapper;
    private final IMealService mealService;
    private final AuditMapper auditMapper;
    private final IAuditManager auditManager;

    @Autowired
    public MealManager(MealMapper mealMapper, IMealService mealService,
                       AuditMapper auditMapper, AuditManager auditManager) {
        this.mealMapper = mealMapper;
        this.mealService = mealService;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
    }

    @Override
    public MealDtoOutput save(MealDtoInput dtoInput) {
        try {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Meal meal = this.mealService.save(mealMapper.inputMapping(dtoInput, userDetails));
            this.prepareAuditToSend(meal, mealPost, userDetails);
            return mealMapper.outputMapping(meal);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mealMapper.outputPageMapping(this.mealService.get(pageable, userDetails.getId()));
    }

    @Override
    public MealDtoOutput get(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return mealMapper.outputMapping(this.mealService.get(id, userDetails.getId()));
    }

    @Override
    public void delete(UUID id) {
        try {
            this.mealService.delete(id);
            this.prepareAuditToSend(id);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public MealDtoOutput update(MealDtoInput dtoInput, UUID id, Long version) {
        try {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Meal meal = this.mealService.update(mealMapper.inputMapping(dtoInput, userDetails), id, version);
            this.prepareAuditToSend(meal, mealPut, userDetails);
            return mealMapper.outputMapping(meal);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    private void prepareAuditToSend(Meal meal, String mealMethod, MyUserDetails userDetails)
            throws JsonProcessingException, URISyntaxException {
        AuditDto auditDto = this.auditMapper.mealOutputMapping(meal, userDetails, mealMethod);
        this.auditManager.audit(auditDto);
    }

    private void prepareAuditToSend(UUID id) throws JsonProcessingException, URISyntaxException {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Meal meal = Meal.builder().id(id).build();
        AuditDto auditDto = this.auditMapper.mealOutputMapping(meal, userDetails, mealDelete);
        this.auditManager.audit(auditDto);
    }
}
