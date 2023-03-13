package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.FileDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ParamsDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.AuditDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.UserDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.mapper.FileMapper;
import aliaksandrkryvapust.reportmicroservice.core.mapper.ReportMapper;
import aliaksandrkryvapust.reportmicroservice.core.mapper.microservices.AuditMapper;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.core.security.UserPrincipal;
import aliaksandrkryvapust.reportmicroservice.repository.api.IReportRepository;
import aliaksandrkryvapust.reportmicroservice.repository.entity.*;
import aliaksandrkryvapust.reportmicroservice.service.api.IAuditManager;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IReportValidator;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.XLSX_CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;
    @Mock
    private IReportRepository reportRepository;
    @Mock
    private IReportValidator reportValidator;
    @Mock
    private ReportMapper reportMapper;
    @Mock
    private IAuditManager auditManager;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private FileMapper fileMapper;

    // preconditions
    final Instant dtCreate = Instant.ofEpochMilli(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochMilli(1673532532870L);
    final String email = "admin@myfit.com";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final String text = "New request for report was created";
    final LocalDate from = LocalDate.parse("2022-12-21");
    final LocalDate to = LocalDate.parse("2023-01-12");
    final String description = "description";
    final EFileType fileType = EFileType.JOURNAL_REPORT;
    final String url = "https://www.example.com";


    @Test
    void save() {
        // preconditions
        final Report reportInput = getPreparedReportInput();
        final Report reportOutput = getPreparedReportOutput();
        Mockito.when(reportRepository.save(reportInput)).thenReturn(reportOutput);

        //test
        Report actual = reportService.save(reportInput);

        // assert
        assertNotNull(actual);
        checkReportOutputFields(actual);
    }

    @Test
    void get() {
        // preconditions
        final Report reportOutput = getPreparedReportOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Report> page = new PageImpl<>(Collections.singletonList(reportOutput), pageable, 1);
        Mockito.when(reportRepository.findAllByUser_Username(pageable, email)).thenReturn(page);

        //test
        Page<Report> actual = reportService.get(pageable, email);

        // assert
        assertNotNull(actual);
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.isFirst());
        for (Report report : actual.getContent()) {
            checkReportOutputFields(report);
        }
    }

    @Test
    void testGet() {
        // preconditions
        final Report reportOutput = getPreparedReportOutput();
        Mockito.when(reportRepository.findByIdAndUser_Username(id, email)).thenReturn(Optional.of(reportOutput));

        //test
        Report actual = reportService.get(id, email);

        // assert
        assertNotNull(actual);
        checkReportOutputFields(actual);
    }

    @Test
    void getReport() {
        // preconditions
        final Report reportOutput = getPreparedReportOutput();
        Mockito.when(reportRepository.findByStatusAndType(EStatus.LOADED, EType.JOURNAL_FOOD)).thenReturn(Optional.of(reportOutput));

        //test
        Optional<Report> actual = reportService.getReport(EStatus.LOADED, EType.JOURNAL_FOOD);

        // assert
        assertNotNull(actual.get());
        actual.ifPresent(this::checkReportOutputFields);
    }

    @Test
    void saveDto() {
        // preconditions
        final ParamsDto dtoInput = getPreparedParamsDto();
        final Report reportInput = getPreparedReportInput();
        final Report reportOutput = getPreparedReportOutput();
        final AuditDto auditDto = getPreparedAuditDto();
        final MyUserDetails userDetails = setSecurityContext();
        Mockito.when(reportMapper.inputMapping(dtoInput, EType.JOURNAL_FOOD, userDetails)).thenReturn(reportInput);
        Mockito.when(reportRepository.save(reportInput)).thenReturn(reportOutput);
        Mockito.when(auditMapper.reportOutputMapping(reportOutput, userDetails, text)).thenReturn(auditDto);
        ArgumentCaptor<Report> actualReport = ArgumentCaptor.forClass(Report.class);
        ArgumentCaptor<AuditDto> actualAudit = ArgumentCaptor.forClass(AuditDto.class);

        //test
        reportService.saveDto(dtoInput, EType.JOURNAL_FOOD);
        Mockito.verify(reportValidator, Mockito.times(1)).validateEntity(actualReport.capture());
        Mockito.verify(auditManager, Mockito.times(1)).audit(actualAudit.capture());

        // assert
        assertEquals(reportInput, actualReport.getValue());
        assertEquals(auditDto, actualAudit.getValue());
    }

    @Test
    void getDto() {
        // preconditions
        final Report reportOutput = getPreparedReportOutput();
        final Pageable pageable = Pageable.ofSize(1).first();
        final Page<Report> page = new PageImpl<>(Collections.singletonList(reportOutput), pageable, 1);
        final PageDtoOutput<ReportDtoOutput> pageDtoOutput = getPreparedPageDtoOutput();
        setSecurityContext();
        Mockito.when(reportRepository.findAllByUser_Username(pageable, email)).thenReturn(page);
        Mockito.when(reportMapper.outputPageMapping(page)).thenReturn(pageDtoOutput);

        //test
        PageDtoOutput<ReportDtoOutput> actual = reportService.getDto(pageable);

        // assert
        assertNotNull(actual);
        checkPageDtoOutputFields(actual);
        actual.getContent().forEach(this::checkReportDtoOutputFields);
    }

    @Test
    void getReportFile() {
        // preconditions
        final FileDtoOutput fileDtoOutput = getPreparedFileDtoOutput();
        final Report reportOutput = getPreparedReportOutput();
        setSecurityContext();
        Mockito.when(reportRepository.findByIdAndUser_Username(id, email)).thenReturn(Optional.of(reportOutput));
        Mockito.when(fileMapper.outputMapping(reportOutput.getFile())).thenReturn(fileDtoOutput);

        //test
        FileDtoOutput actual = reportService.getReportFile(id);

        // assert
        assertNotNull(actual);
        checkFileDtoOutputFields(actual);
    }

    private MyUserDetails setSecurityContext() {
        final MyUserDetails userDetails = getPreparedUserDetails();
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
        return userDetails;
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


    private Params getPreparedParamsOutput() {
        return Params.builder()
                .start(from)
                .finish(to)
                .build();
    }

    private FileDtoOutput getPreparedFileDtoOutput() {
        return FileDtoOutput.builder()
                .fileType(fileType.name())
                .contentType(XLSX_CONTENT_TYPE)
                .fileName(id.toString())
                .url(url)
                .build();
    }

    private File getPreparedFile() {
        return File.builder()
                .fileType(fileType)
                .contentType(XLSX_CONTENT_TYPE)
                .fileName(id.toString())
                .url(url)
                .build();
    }

    private Report getPreparedReportOutput() {
        return Report.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .id(id)
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description(description)
                .params(getPreparedParamsOutput())
                .file(getPreparedFile())
                .build();
    }

    private ParamsDto getPreparedParamsDto() {
        return ParamsDto.builder()
                .from(from)
                .to(to)
                .build();
    }

    private Report getPreparedReportInput() {
        return Report.builder()
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description(description)
                .params(getPreparedParamsOutput())
                .build();
    }

    private ParamsDtoOutput getPreparedParamsDtoOutput() {
        return ParamsDtoOutput.builder()
                .from(from)
                .to(to)
                .build();
    }

    private ReportDtoOutput getPreparedReportDtoOutput() {
        return ReportDtoOutput.builder()
                .dtCreate(dtCreate.toEpochMilli())
                .dtUpdate(dtUpdate.toEpochMilli())
                .id(id.toString())
                .status(EStatus.LOADED.name())
                .type(EType.JOURNAL_FOOD.name())
                .description(description)
                .params(getPreparedParamsDtoOutput())
                .build();
    }

    PageDtoOutput<ReportDtoOutput> getPreparedPageDtoOutput() {
        return PageDtoOutput.<ReportDtoOutput>builder()
                .number(2)
                .size(1)
                .totalPages(1)
                .totalElements(1L)
                .first(true)
                .numberOfElements(1)
                .last(true)
                .content(Collections.singletonList(getPreparedReportDtoOutput()))
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

    private void checkReportOutputFields(Report actual) {
        assertEquals(id, actual.getId());
        assertEquals(EStatus.LOADED, actual.getStatus());
        assertEquals(EType.JOURNAL_FOOD, actual.getType());
        assertEquals(description, actual.getDescription());
        assertEquals(dtCreate, actual.getDtCreate());
        assertEquals(dtUpdate, actual.getDtUpdate());
        assertEquals(fileType, actual.getFile().getFileType());
        assertEquals(XLSX_CONTENT_TYPE, actual.getFile().getContentType());
        assertEquals(id.toString(), actual.getFile().getFileName());
        assertEquals(url, actual.getFile().getUrl());
        assertEquals(from, actual.getParams().getStart());
        assertEquals(to, actual.getParams().getFinish());
    }

    private void checkReportDtoOutputFields(ReportDtoOutput actual) {
        assertEquals(id.toString(), actual.getId());
        assertEquals(EStatus.LOADED.name(), actual.getStatus());
        assertEquals(EType.JOURNAL_FOOD.name(), actual.getType());
        assertEquals(description, actual.getDescription());
        assertEquals(dtCreate.toEpochMilli(), actual.getDtCreate());
        assertEquals(dtUpdate.toEpochMilli(), actual.getDtUpdate());
        assertEquals(from, actual.getParams().getFrom());
        assertEquals(to, actual.getParams().getTo());
    }

    private void checkPageDtoOutputFields(PageDtoOutput<ReportDtoOutput> actual) {
        assertEquals(1, actual.getTotalPages());
        Assertions.assertTrue(actual.getFirst());
        Assertions.assertTrue(actual.getLast());
        assertEquals(2, actual.getNumber());
        assertEquals(1, actual.getNumberOfElements());
        assertEquals(1, actual.getSize());
        assertEquals(1, actual.getTotalPages());
        assertEquals(1, actual.getTotalElements());
    }

    private void checkFileDtoOutputFields(FileDtoOutput actual) {
        assertEquals(fileType.name(), actual.getFileType());
        assertEquals(XLSX_CONTENT_TYPE, actual.getContentType());
        assertEquals(id.toString(), actual.getFileName());
        assertEquals(url, actual.getUrl());
    }
}