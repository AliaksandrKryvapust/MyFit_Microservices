package aliaksandrkryvapust.reportmicroservice.core.mapper;

import aliaksandrkryvapust.reportmicroservice.core.dto.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Params;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Status;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportMapper {

    public Report inputMapping(ParamsDto paramsDto, Type type, String username) {
        String from = paramsDto.getFrom().toString();
        String to = paramsDto.getTo().toString();
        Params params = Params.builder()
                .start((paramsDto.getFrom()))
                .finish(paramsDto.getTo()).build();
        if (type.name().equals("JOURNAL_FOOD")) {
            String description = String.format("Meal records of user %s from %s to %s.", username, from, to);
            return Report.builder()
                    .params(params)
                    .type(type)
                    .username(username)
                    .status(Status.LOADED)
                    .description(description).build();
        } else {
            throw new IllegalStateException("Type field is not enum value");
        }

    }

    public ReportDtoOutput outputMapping(Report report) {
        ParamsDto paramsDto = ParamsDto.builder()
                .from(report.getParams().getStart())
                .to(report.getParams().getFinish()).build();
        return ReportDtoOutput.builder()
                .id(report.getId())
                .params(paramsDto)
                .description(report.getDescription())
                .type(report.getType())
                .status(report.getStatus())
                .dtCreate(report.getDtCreate().toEpochMilli())
                .dtUpdate(report.getDtUpdate().toEpochMilli())
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
