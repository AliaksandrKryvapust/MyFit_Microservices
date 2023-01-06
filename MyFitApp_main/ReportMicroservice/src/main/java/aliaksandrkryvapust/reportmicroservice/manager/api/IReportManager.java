package aliaksandrkryvapust.reportmicroservice.manager.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.ParamsDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.ReportDtoOutput;

import java.util.UUID;

public interface IReportManager extends IManager<ReportDtoOutput, ParamsDto>{
    byte[] getReportFile(UUID id);
}
