package aliaksandrkryvapust.reportmicroservice.repository.api;

import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IReportRepository extends JpaRepository<Report, UUID> {
    @Query("SELECT new Report(rep.fileValue) FROM Report rep WHERE rep.id =?1")
    Report fileExport(UUID id);

    Page<Report> findAllByUsername(Pageable pageable, String username);

    Optional<Report> findByStatusAndType(EStatus status, EType type);
}
