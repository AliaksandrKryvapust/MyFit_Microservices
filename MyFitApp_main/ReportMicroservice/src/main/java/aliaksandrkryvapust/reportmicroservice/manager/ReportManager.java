package aliaksandrkryvapust.reportmicroservice.manager;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.AuditDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.mapper.ReportMapper;
import aliaksandrkryvapust.reportmicroservice.core.mapper.microservices.AuditMapper;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.manager.api.IReportManager;
import aliaksandrkryvapust.reportmicroservice.manager.audit.AuditManager;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.UUID;

@Component
public class ReportManager implements IReportManager {
    private final static String reportPost = "New request for report was created";
    private final IReportService reportService;
    private final ReportMapper reportMapper;
    private final AuditMapper auditMapper;
    private final AuditManager auditManager;

    public ReportManager(IReportService reportService, ReportMapper reportMapper, AuditMapper auditMapper,
                         AuditManager auditManager) {
        this.reportService = reportService;
        this.reportMapper = reportMapper;
        this.auditMapper = auditMapper;
        this.auditManager = auditManager;
    }

    @Override
    public ReportDtoOutput save(ParamsDto paramsDto, EType type) {
        try {
            MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Report report = this.reportService.save(reportMapper.inputMapping(paramsDto, type, userDetails));
            this.prepareAuditToSend(report, userDetails);
            return this.reportMapper.outputMapping(report);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI to audit is incorrect");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert to JSON");
        }
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.reportMapper.outputPageMapping(reportService.get(pageable, userDetails.getUsername()));
    }

    @Override
    public ReportDtoOutput get(UUID id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.reportMapper.outputMapping(reportService.get(id, userDetails.getUsername()));
    }

    @Override
    public byte[] getReportFile(UUID id) {
        return this.reportService.exportFile(id);
    }

    private void prepareAuditToSend(Report report, MyUserDetails userDetails)
            throws JsonProcessingException, URISyntaxException {
        AuditDto auditDto = this.auditMapper.reportOutputMapping(report, userDetails, ReportManager.reportPost);
        this.auditManager.audit(auditDto);
    }
}
