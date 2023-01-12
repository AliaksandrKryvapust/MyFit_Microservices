package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import aliaksandrkryvapust.reportmicroservice.repository.api.IReportRepository;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReportService implements IReportService {
    private final IReportRepository reportRepository;

    @Autowired
    public ReportService(IReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    @Transactional
    public Report save(Report report) {
        return this.reportRepository.save(report);
    }

    @Override
    public Page<Report> get(Pageable pageable, MyUserDetails userDetails) {
        Page<Report> reports = this.reportRepository.findAllByUser_Username(pageable, userDetails.getUsername());
        this.checkCredentials(userDetails, reports);
        return reports;
    }

    @Override
    public Report get(UUID id, MyUserDetails userDetails) {
        Report report = this.reportRepository.findById(id).orElseThrow();
        this.checkCredential(userDetails, report);
        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportFile(UUID id) {
        return this.reportRepository.fileExport(id).getFileValue();
    }

    @Override
    public Optional<Report> getReport(EStatus status, EType type) {
        return this.reportRepository.findByStatusAndType(status, type);
    }

    @Override
    public Report getWithoutCredentialsCheck(UUID id) {
        return this.reportRepository.findById(id).orElseThrow();
    }

    private void checkCredentials(MyUserDetails userDetails, Page<Report> reports) {
        reports.forEach((i) -> this.checkCredential(userDetails, i));
    }

    private void checkCredential(MyUserDetails userDetails, Report report) {
        if (!report.getUser().getUserId().equals(userDetails.getId())) {
            throw new BadCredentialsException("It`s forbidden to modify not private data");
        }
    }
}
