package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Status;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;

import java.util.Optional;

public interface IReportService extends IService<Report> {
    Optional<Report> getReport(Status status, Type type);
}
