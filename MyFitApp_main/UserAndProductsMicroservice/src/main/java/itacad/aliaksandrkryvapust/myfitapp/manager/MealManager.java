package itacad.aliaksandrkryvapust.myfitapp.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import itacad.aliaksandrkryvapust.myfitapp.controller.utils.JwtTokenUtil;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.input.MealDtoInput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.MealDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.myfitapp.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.MealMapper;
import itacad.aliaksandrkryvapust.myfitapp.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.myfitapp.manager.api.IMealManager;
import itacad.aliaksandrkryvapust.myfitapp.manager.audit.AuditManager;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Meal;
import itacad.aliaksandrkryvapust.myfitapp.repository.entity.User;
import itacad.aliaksandrkryvapust.myfitapp.service.UserService;
import itacad.aliaksandrkryvapust.myfitapp.service.api.IMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class MealManager implements IMealManager {
    private final static String mealPost = "New meal was created";
    private final static String mealPut = "Meal was updated";
    private final static String mealDelete = "Meal was deleted";
    private final MealMapper mealMapper;
    private final IMealService mealService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final AuditMapper auditMapper;
    private final AuditManager auditManager;

    @Autowired
    public MealManager(MealMapper mealMapper, IMealService mealService, JwtTokenUtil jwtTokenUtil,
                       UserService userService, AuditMapper auditMapper, AuditManager auditManager) {
        this.mealMapper = mealMapper;
        this.mealService = mealService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
    }

    @Override
    public MealDtoOutput save(MealDtoInput dtoInput, HttpServletRequest request) {
        try {
            Meal meal = this.mealService.save(mealMapper.inputMapping(dtoInput));
            User user = getUser(request);
            AuditDto auditDto = this.auditMapper.mealOutputMapping(meal, user, mealPost);
            this.auditManager.audit(auditDto);
            return mealMapper.outputMapping(meal);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    private User getUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username = jwtTokenUtil.getUsername(token);
        return this.userService.getUser(username);
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        return mealMapper.outputPageMapping(this.mealService.get(pageable));
    }

    @Override
    public MealDtoOutput get(UUID id) {
        return mealMapper.outputMapping(this.mealService.get(id));
    }

    @Override
    public void delete(UUID id, HttpServletRequest request) {
        try {
            this.mealService.delete(id);
            User user = getUser(request);
            Meal meal = Meal.builder().id(id).build();
            AuditDto auditDto = this.auditMapper.mealOutputMapping(meal, user, mealDelete);
            this.auditManager.audit(auditDto);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public MealDtoOutput update(MealDtoInput dtoInput, UUID id, Long version, HttpServletRequest request) {
        try {
            Meal meal = this.mealService.update(mealMapper.inputMapping(dtoInput), id, version);
            User user = getUser(request);
            AuditDto auditDto = this.auditMapper.mealOutputMapping(meal, user, mealPut);
            this.auditManager.audit(auditDto);
            return mealMapper.outputMapping(meal);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }
}
