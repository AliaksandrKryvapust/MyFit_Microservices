package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.FileDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.AuditDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.mapper.FileMapper;
import aliaksandrkryvapust.reportmicroservice.core.mapper.ReportMapper;
import aliaksandrkryvapust.reportmicroservice.core.mapper.microservices.AuditMapper;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.repository.api.IReportRepository;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.service.api.IAuditManager;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportManager;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IReportValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService, IReportManager {
    private final static String REPORT_POST = "New request for report was created";
    private final IReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final IReportValidator reportValidator;
    private final AuditMapper auditMapper;
    private final IAuditManager auditManager;
    private final FileMapper fileMapper;

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Page<Report> get(Pageable pageable, String username) {
        return reportRepository.findAllByUser_Username(pageable, username);
    }

    @Override
    public Report get(UUID id, String username) {
        return reportRepository.findByIdAndUser_Username(id, username).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Optional<Report> getReport(EStatus status, EType type) {
        return reportRepository.findByStatusAndType(status, type);
    }

    @Override
    public void saveDto(ParamsDto paramsDto, EType type) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Report entityToSave = reportMapper.inputMapping(paramsDto, type, userDetails);
        reportValidator.validateEntity(entityToSave);
        Report report = save(entityToSave);
        prepareAudit(report, userDetails, REPORT_POST);
    }

    @Override
    public PageDtoOutput<ReportDtoOutput> getDto(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Report> page = get(pageable, userDetails.getUsername());
        return reportMapper.outputPageMapping(page);
    }

    @Override
    public FileDtoOutput getReportFile(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Report report = get(id, userDetails.getUsername());
        return fileMapper.outputMapping(report.getFile());
    }

    private void prepareAudit(Report report, MyUserDetails userDetails, String method) {
        AuditDto auditDto = auditMapper.reportOutputMapping(report, userDetails, method);
        auditManager.audit(auditDto);
    }
}
