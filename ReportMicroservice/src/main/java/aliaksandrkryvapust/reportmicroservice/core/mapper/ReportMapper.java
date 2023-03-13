package aliaksandrkryvapust.reportmicroservice.core.mapper;

import aliaksandrkryvapust.reportmicroservice.core.aws.AwsS3FileDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ParamsDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.mapper.microservices.UserMapper;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.repository.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.XLSX_CONTENT_TYPE;

@Component
@RequiredArgsConstructor
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportMapper {
    private final UserMapper userMapper;

    public Report inputMapping(ParamsDto paramsDto, EType type, MyUserDetails userDetails) {
        String from = paramsDto.getFrom().toString();
        String to = paramsDto.getTo().toString();
        Params params = Params.builder()
                .start((paramsDto.getFrom()))
                .finish(paramsDto.getTo())
                .build();
        User user = userMapper.inputMapping(userDetails);
        if (type.name().equals("JOURNAL_FOOD")) {
            String description = String.format("Meal records of user %s from %s to %s.", userDetails.getUsername(), from, to);
            return Report.builder()
                    .params(params)
                    .type(type)
                    .user(user)
                    .status(EStatus.LOADED)
                    .description(description)
                    .build();
        } else {
            throw new IllegalStateException("EType field is not enum value");
        }

    }

    public Report setJournalReport(Report report, AwsS3FileDto fileDto) {
        File file = File.builder()
                .fileType(EFileType.JOURNAL_REPORT)
                .contentType(XLSX_CONTENT_TYPE)
                .fileName(report.getId().toString())
                .url(fileDto.getUrl())
                .fileKey(fileDto.getFileKey())
                .build();
        report.setFile(file);
        return report;
    }

    public ReportDtoOutput outputMapping(Report report) {
        ParamsDtoOutput paramsDto = ParamsDtoOutput.builder()
                .from(report.getParams().getStart())
                .to(report.getParams().getFinish())
                .build();
        return ReportDtoOutput.builder()
                .id(report.getId().toString())
                .params(paramsDto)
                .description(report.getDescription())
                .type(report.getType().name())
                .status(report.getStatus().name())
                .dtCreate(report.getDtCreate().toEpochMilli())
                .dtUpdate(report.getDtUpdate().toEpochMilli())
                .build();
    }

    public PageDtoOutput<ReportDtoOutput> outputPageMapping(Page<Report> reports) {
        List<ReportDtoOutput> outputs = reports.getContent().stream()
                .map(this::outputMapping)
                .collect(Collectors.toList());
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
