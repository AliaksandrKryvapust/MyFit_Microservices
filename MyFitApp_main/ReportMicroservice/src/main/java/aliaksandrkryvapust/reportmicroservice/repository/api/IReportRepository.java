package aliaksandrkryvapust.reportmicroservice.repository.api;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Status;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IReportRepository extends JpaRepository<Report, UUID> {
    Optional<Report> findByStatusAndType(Status status, Type type);
}
