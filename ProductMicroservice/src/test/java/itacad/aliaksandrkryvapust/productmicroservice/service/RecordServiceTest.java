package itacad.aliaksandrkryvapust.productmicroservice.service;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.export.ParamsDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.RecordDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.input.references.MealReferenceDtoInput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.ProductDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.RecordDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.AuditDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.EType;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.RecordDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.microservices.UserDto;
import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.RecordMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.mapper.microservices.AuditMapper;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.MyUserDetails;
import itacad.aliaksandrkryvapust.productmicroservice.core.security.UserPrincipal;
import itacad.aliaksandrkryvapust.productmicroservice.repository.api.IRecordRepository;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EUserRole;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Product;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IAuditManager;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProductService;
import itacad.aliaksandrkryvapust.productmicroservice.service.api.IProfileService;
import itacad.aliaksandrkryvapust.productmicroservice.service.validator.api.IRecordValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {
    @InjectMocks
    private RecordService recordService;
    @Mock
    private IProductService productService;
    @Mock
    private IRecordRepository recordRepository;
    @Mock
    private IRecordValidator recordValidator;
    @Mock
    private RecordMapper recordMapper;
    @Mock
    private IAuditManager auditManager;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private IProfileService profileService;
    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String text = "New record was created";
    final String title = "cheese";
    final Integer calories = 300;
    final Double proteins = 33.0;
    final Double fats = 28.0;
    final Double carbohydrates = 0.0;
    final Integer weight = 100;
    final String dt_Supply = "1993-07-01";

    @Test
    void save() {
        // preconditions
        final Record recordInput = getPreparedRecordInput();
        final Record recordOutput = getPreparedRecordOutput();
        final Product product = getPreparedProductOutput();
        Mockito.when(productService.get(id, id)).thenReturn(product);
        Mockito.when(recordRepository.save(recordInput)).thenReturn(recordOutput);

        //test
        Record actual = recordService.save(recordInput);

        // assert
        assertNotNull(actual);
        checkRecordOutputFields(actual);
    }

    @Test
    void get() {
        // preconditions
        final Record recordOutput = getPreparedRecordOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Record> page = new PageImpl<>(Collections.singletonList(recordOutput), pageable, 1);
        Mockito.when(recordRepository.findAllByUserId(pageable, id)).thenReturn(page);

        //test
        Page<Record> actual = recordService.get(pageable, id);

        // assert
        assertNotNull(actual);
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.isFirst());
        for (Record record : actual.getContent()) {
            checkRecordOutputFields(record);
        }
    }

    @Test
    void testGet() {
        // preconditions
        final Record recordOutput = getPreparedRecordOutput();
        Mockito.when(recordRepository.findByIdAndUserId(id, id)).thenReturn(Optional.of(recordOutput));

        //test
        Record actual = recordService.get(id, id);

        // assert
        assertNotNull(actual);
        checkRecordOutputFields(actual);
    }

    @Test
    void getRecordByTimeGap() {
        // preconditions
        final Record recordOutput = getPreparedRecordOutput();
        final ParamsDto paramsDto = getPreparedParamsDto();
        Mockito.when(recordRepository.getRecordByTimeGap(dtCreate, dtUpdate, id)).thenReturn(Collections.singletonList(recordOutput));

        //test
        List<Record> actual = recordService.getRecordByTimeGap(paramsDto);

        // assert
        assertNotNull(actual);
        actual.forEach(this::checkRecordOutputFields);
    }

    @Test
    void saveDto() {
        // preconditions
        final RecordDtoInput dtoInput = getPreparedRecordDtoInput();
        final RecordDtoOutput dtoOutput = getPreparedRecordDtoOutput();
        final Record recordInput = getPreparedRecordInput();
        final Record recordOutput = getPreparedRecordOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        final MyUserDetails userDetails = setSecurityContext();
        final Product product = getPreparedProductOutput();
        Mockito.when(recordMapper.inputMapping(dtoInput, userDetails)).thenReturn(recordInput);
        Mockito.when(productService.get(id, id)).thenReturn(product);
        Mockito.when(recordRepository.save(recordInput)).thenReturn(recordOutput);
        Mockito.when(auditMapper.recordOutputMapping(recordOutput, userDetails, text)).thenReturn(auditDto);
        Mockito.when(recordMapper.outputMapping(recordOutput)).thenReturn(dtoOutput);
        ArgumentCaptor<Record> actualRecord = ArgumentCaptor.forClass(Record.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        RecordDtoOutput actual = recordService.saveDto(dtoInput, id);
        Mockito.verify(profileService, Mockito.times(1)).get(id, id);
        Mockito.verify(recordValidator, Mockito.times(1)).validateEntity(actualRecord.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(recordInput, actualRecord.getValue());
        assertEquals(auditDto, actualAudit.getValue());
        assertNotNull(actual);
        checkRecordDtoOutputFields(actual);
    }

    @Test
    void getDto() {
        // preconditions
        final Record recordOutput = getPreparedRecordOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Record> page = new PageImpl<>(Collections.singletonList(recordOutput), pageable, 1);
        final PageDtoOutput<RecordDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        setSecurityContext();
        Mockito.when(recordRepository.findAllByUserId(pageable, id)).thenReturn(page);
        Mockito.when(recordMapper.outputPageMapping(page)).thenReturn(pageDtoOutput);

        //test
        PageDtoOutput<RecordDtoOutput> actual = recordService.getDto(pageable, id);
        Mockito.verify(profileService, Mockito.times(1)).get(id, id);

        // assert
        assertNotNull(actual);
        checkPageDtoOutputFields(actual);
        actual.getContent().forEach(this::checkRecordDtoOutputFields);
    }

    @Test
    void getRecord() {
        // preconditions
        final Record recordOutput = getPreparedRecordOutput();
        final ParamsDto paramsDto = getPreparedParamsDto();
        final RecordDto recordDto = getPreparedRecordDto();
        Mockito.when(recordRepository.getRecordByTimeGap(dtCreate, dtUpdate, id)).thenReturn(Collections.singletonList(recordOutput));
        Mockito.when(recordMapper.listOutputMapping(Collections.singletonList(recordOutput)))
                .thenReturn(Collections.singletonList(recordDto));

        //test
        List<RecordDto> actual = recordService.getRecord(paramsDto);

        // assert
        assertNotNull(actual);
        actual.forEach(this::checkRecordDtoFields);
    }

    private MyUserDetails setSecurityContext() {
        final MyUserDetails userDetails = getPreparedUserDetails();
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
        return userDetails;
    }

    private Product getPreparedProductOutput() {
        return Product.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .id(id)
                .build();
    }

    private Record getPreparedRecordInput() {
        return Record.builder()
                .weight(weight)
                .dtSupply(dtUpdate)
                .userId(id)
                .productId(id)
                .product(getPreparedProductOutput())
                .build();
    }

    private Record getPreparedRecordOutput() {
        return Record.builder()
                .dtCreate(dtCreate)
                .dtSupply(dtUpdate)
                .product(getPreparedProductOutput())
                .weight(weight)
                .id(id)
                .build();
    }

    private ParamsDto getPreparedParamsDto() {
        return ParamsDto.builder()
                .userId(id.toString())
                .from(dtCreate)
                .to(dtUpdate)
                .build();
    }

    private RecordDto getPreparedRecordDto() {
        return RecordDto.builder()
                .weight(weight)
                .dtSupply(dt_Supply)
                .product(getPreparedProductDtoOutput())
                .build();
    }

    private MealReferenceDtoInput getPreparedMealReferenceDtoInput() {
        return MealReferenceDtoInput.builder()
                .id(id.toString())
                .build();
    }

    private RecordDtoInput getPreparedRecordDtoInput() {
        return RecordDtoInput.builder()
                .dtSupply(dtUpdate)
                .recipe(getPreparedMealReferenceDtoInput())
                .weight(weight)
                .build();
    }

    private ProductDtoOutput getPreparedProductDtoOutput() {
        return ProductDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .title(title)
                .calories(calories)
                .proteins(proteins)
                .fats(fats)
                .carbohydrates(carbohydrates)
                .weight(weight)
                .id(id.toString())
                .build();
    }

    private RecordDtoOutput getPreparedRecordDtoOutput() {
        return RecordDtoOutput.builder()
                .dtCreate(dtCreate)
                .dtSupply(dtUpdate)
                .product(getPreparedProductDtoOutput())
                .weight(weight)
                .id(id.toString())
                .build();
    }

    PageDtoOutput<RecordDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<RecordDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedRecordDtoOutput()))
                .build();
    }

    private UserDto getPreparedUserDto() {
        return UserDto.builder()
                .role(EUserRole.USER.name())
                .email(email)
                .id(id.toString())
                .build();
    }

    private AuditDto getPreparedAuditDto() {
        return AuditDto.builder()
                .id(String.valueOf(id))
                .user(getPreparedUserDto())
                .text(text)
                .type(EType.PRODUCT.name())
                .build();
    }

    private MyUserDetails getPreparedUserDetails() {
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id(id)
                .username(email)
                .authenticated(true)
                .role(EUserRole.USER)
                .dtUpdate(dtUpdate)
                .build();
        return new MyUserDetails(userPrincipal);
    }

    private void checkRecordOutputFields(Record actual) {
        assertEquals(id, actual.getId());
        assertEquals(weight, actual.getWeight());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtSupply());
        assertNotNull(actual.getProduct());
        assertEquals(id, actual.getProduct().getId());
        assertEquals(weight, actual.getProduct().getWeight());
        assertEquals(carbohydrates, actual.getProduct().getCarbohydrates());
        assertEquals(fats, actual.getProduct().getFats());
        assertEquals(proteins, actual.getProduct().getProteins());
        assertEquals(calories, actual.getProduct().getCalories());
        assertEquals(title, actual.getProduct().getTitle());
        assertEquals(dtCreate, actual.getProduct().getDtCreate());
        assertEquals(dtUpdate, actual.getProduct().getDtUpdate());
    }

    private void checkRecordDtoOutputFields(RecordDtoOutput actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(weight, actual.getWeight());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtSupply());
        assertNotNull(actual.getProduct());
        assertEquals(id.toString(), actual.getProduct().getId());
        assertEquals(weight, actual.getProduct().getWeight());
        assertEquals(carbohydrates, actual.getProduct().getCarbohydrates());
        assertEquals(fats, actual.getProduct().getFats());
        assertEquals(proteins, actual.getProduct().getProteins());
        assertEquals(calories, actual.getProduct().getCalories());
        assertEquals(title, actual.getProduct().getTitle());
        assertEquals(dtCreate, actual.getProduct().getDtCreate());
        assertEquals(dtUpdate, actual.getProduct().getDtUpdate());
    }

    private void checkPageDtoOutputFields(PageDtoOutput<RecordDtoOutput> actual) {
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.getFirst());
        Assertions.assertTrue(actual.getLast());
        assertEquals(2, actual.getNumber());
        assertEquals(1, actual.getNumberOfElements());
        assertEquals(1, actual.getSize());
        assertEquals(1, actual.getTotalPages());
        assertEquals(1, actual.getTotalElements());
    }

    private void checkRecordDtoFields(RecordDto actual) {
        assertEquals(weight, actual.getWeight());
        assertEquals(dt_Supply, actual.getDtSupply());
        assertNotNull(actual.getProduct());
        assertEquals(id.toString(), actual.getProduct().getId());
        assertEquals(weight, actual.getProduct().getWeight());
        assertEquals(carbohydrates, actual.getProduct().getCarbohydrates());
        assertEquals(fats, actual.getProduct().getFats());
        assertEquals(proteins, actual.getProduct().getProteins());
        assertEquals(calories, actual.getProduct().getCalories());
        assertEquals(title, actual.getProduct().getTitle());
        assertEquals(dtCreate, actual.getProduct().getDtCreate());
        assertEquals(dtUpdate, actual.getProduct().getDtUpdate());
    }
}