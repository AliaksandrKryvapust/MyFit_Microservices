package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;

import java.util.Optional;
import java.util.UUID;

public interface IReportService extends IService<Report> {
    byte[] exportFile(UUID id);
    Optional<Report> getReport(EStatus status, EType type);
}
