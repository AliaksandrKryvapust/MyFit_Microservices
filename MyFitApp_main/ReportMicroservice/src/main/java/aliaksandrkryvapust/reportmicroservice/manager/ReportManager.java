package aliaksandrkryvapust.reportmicroservice.manager;

import aliaksandrkryvapust.reportmicroservice.core.dto.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.mapper.ReportMapper;
import aliaksandrkryvapust.reportmicroservice.manager.api.IReportManager;
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
    public ReportDtoOutput save(ParamsDto type) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDetails.g
        return null;
    }

    @Override
    public PageDtoOutput get(Pageable pageable) {
        return this.reportMapper.outputPageMapping(reportService.get(pageable));
    }

    @Override
    public ReportDtoOutput get(UUID id) {
        return this.reportMapper.outputMapping(reportService.get(id));
    }
}
