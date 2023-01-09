package aliaksandrkryvapust.reportmicroservice.manager;

import aliaksandrkryvapust.reportmicroservice.core.dto.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.mapper.ReportMapper;
import aliaksandrkryvapust.reportmicroservice.manager.api.IReportManager;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReportManager implements IReportManager {
    private final IReportService reportService;
    private final ReportMapper reportMapper;

    @Autowired
    public ReportManager(IReportService reportService, ReportMapper reportMapper) {
        this.reportService = reportService;
        this.reportMapper = reportMapper;
    }

    @Override
    public ReportDtoOutput save(ParamsDto paramsDto, Type type) {
        String username = this.getUsername();
        Report report = this.reportService.save(reportMapper.inputMapping(paramsDto, type, username));
        return this.reportMapper.outputMapping(report); //TODO add audit
    }

    private String getUsername() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        String username = this.getUsername();
        return this.reportMapper.outputPageMapping(reportService.get(pageable, username));
    }

    @Override
    public ReportDtoOutput get(UUID id) {
        String username = this.getUsername();
        return this.reportMapper.outputMapping(reportService.get(id, username));
    }

    @Override
    public byte[] getReportFile(UUID id) {
        return this.reportService.exportFile(id);
    }
}
