package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.repository.api.IReportRepository;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Status;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.AccessControlException;
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
    public Page<Report> get(Pageable pageable, String username) {
        return this.reportRepository.findAllByUsername(pageable, username);
    }

    @Override
    public Report get(UUID id, String username) {
        Report report = this.reportRepository.findById(id).orElseThrow();
        if (!report.getUsername().equals(username)) {
            throw new AccessControlException("Forbidden, authorised user and report don`t match");
        }
        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportFile(UUID id) {
        return this.reportRepository.fileExport(id).getFileValue();
    }

    @Override
    public Optional<Report> getReport(Status status, Type type) {
        return this.reportRepository.findByStatusAndType(status, type);
    }
}
