package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Status;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;

import java.util.Optional;
import java.util.UUID;

public interface IReportService extends IService<Report> {
    byte[] exportFile(UUID id);
    Optional<Report> getReport(Status status, Type type);
}
