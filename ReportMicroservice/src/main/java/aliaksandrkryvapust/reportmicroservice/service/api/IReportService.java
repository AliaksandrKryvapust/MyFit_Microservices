package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;

import java.util.Optional;
import java.util.UUID;

public interface IReportService extends IService<Report> {
    byte[] exportFile(UUID id);
    Optional<Report> getReport(EStatus status, EType type);
    Report getWithoutCredentialsCheck(UUID id);
}
