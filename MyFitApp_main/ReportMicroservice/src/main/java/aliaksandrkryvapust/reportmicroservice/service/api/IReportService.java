package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.repository.entity.EType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;

import java.util.Optional;
import java.util.UUID;

public interface IReportService extends IService<Report> {
    byte[] exportFile(UUID id);
    Optional<Report> getReport(EStatus status, EType type);
}
