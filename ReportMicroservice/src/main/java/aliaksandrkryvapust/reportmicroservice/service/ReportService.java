package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.repository.api.IReportRepository;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {
    private final IReportRepository reportRepository;

    @Override
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Page<Report> get(Pageable pageable, String username) {
        return reportRepository.findAllByUser_Username(pageable, username);
    }

    @Override
    public Report get(UUID id, String username) {
        return reportRepository.findByIdAndUser_Username(id, username).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public byte[] exportFile(UUID id) {
        return reportRepository.fileExport(id).getFileValue();
    }

    @Override
    public Optional<Report> getReport(EStatus status, EType type) {
        return reportRepository.findByStatusAndType(status, type);
    }
}
