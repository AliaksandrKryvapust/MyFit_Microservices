package aliaksandrkryvapust.reportmicroservice.core.mapper;

import aliaksandrkryvapust.reportmicroservice.core.dto.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportMapper {

    public Report inputMapping(ParamsDto paramsDto){
        return Report.builder().
    }

    public ReportDtoOutput outputMapping(Report report) {
        ParamsDto paramsDto = ParamsDto.builder()
                .from(report.getParams().getFrom())
                .to(report.getParams().getTo()).build();
        return ReportDtoOutput.builder()
                .id(report.getId())
                .params(paramsDto)
                .description(report.getDescription())
                .type(report.getType())
                .status(report.getStatus())
                .dtCreate(report.getDtCreate())
                .dtUpdate(report.getDtUpdate())
                .build();
    }

    public PageDtoOutput<ReportDtoOutput> outputPageMapping(Page<Report> reports) {
        List<ReportDtoOutput> outputs = reports.getContent().stream().map(this::outputMapping).collect(Collectors.toList());
        return PageDtoOutput.<ReportDtoOutput>builder()
                .number(reports.getNumber() + 1)
                .size(reports.getSize())
                .totalPages(reports.getTotalPages())
                .totalElements(reports.getTotalElements())
                .first(reports.isFirst())
                .numberOfElements(reports.getNumberOfElements())
                .last(reports.isLast())
                .content(outputs)
                .build();
    }
}
