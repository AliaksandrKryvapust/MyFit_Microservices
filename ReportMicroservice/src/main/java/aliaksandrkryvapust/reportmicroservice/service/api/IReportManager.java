package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.input.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.ReportDtoOutput;
import aliaksandrkryvapust.reportmicroservice.service.api.IManager;

import java.util.UUID;

public interface IReportManager extends IManager<ReportDtoOutput, ParamsDto> {
    byte[] getReportFile(UUID id);
}
