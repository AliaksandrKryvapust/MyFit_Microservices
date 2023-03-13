package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.FileDtoOutput;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;

import java.util.UUID;

public interface IReportManager extends IManager<ReportDtoOutput, ParamsDto> {
    FileDtoOutput getReportFile(UUID id);
}
