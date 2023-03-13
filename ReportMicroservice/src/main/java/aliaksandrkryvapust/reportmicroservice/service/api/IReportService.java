package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;

import java.util.Optional;

public interface IReportService extends IService<Report> {
    Optional<Report> getReport(EStatus status, EType type);
}
