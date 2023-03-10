package aliaksandrkryvapust.reportmicroservice.repository.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IReportRepository extends JpaRepository<Report, UUID> {
    @Transactional(readOnly = true)
    @Query("SELECT new Report(rep.fileValue) FROM Report rep WHERE rep.id =?1")
    Report fileExport(UUID id);

    @Transactional(readOnly = true)
    Page<Report> findAllByUser_Username(Pageable pageable, String username);

    @Transactional(readOnly = true)
    Optional<Report> findByStatusAndType(EStatus status, EType type);
    @Transactional(readOnly = true)
    Optional<Report> findByIdAndUser_Username(UUID id, String username);
}
