package aliaksandrkryvapust.reportmicroservice.job;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.RecordDto;
import aliaksandrkryvapust.reportmicroservice.core.mapper.poi.XlsxRecordMapper;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Status;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import aliaksandrkryvapust.reportmicroservice.service.api.IXlsxRecordService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.JOB_IMPORT_URI;
import static aliaksandrkryvapust.reportmicroservice.core.Constants.TOKEN_HEADER;

@Slf4j
@Component
public class ReportLoadJob implements Job {
    private static final String jwtSecret = "NDQ1ZjAzNjQtMzViZi00MDRjLTljZjQtNjNjYWIyZTU5ZDYw";
    private final IReportService reportService;
    private final IXlsxRecordService xlsxRecordService;
    private final XlsxRecordMapper recordMapper;

    public ReportLoadJob(IReportService reportService, IXlsxRecordService xlsxRecordService, XlsxRecordMapper recordMapper) {
        this.reportService = reportService;
        this.xlsxRecordService = xlsxRecordService;
        this.recordMapper = recordMapper;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Report report = new Report();
        try {
            log.info("Report Job started");
            UUID id;
            try {
                report = this.reportService.getReport(Status.LOADED, Type.JOURNAL_FOOD).orElseThrow();
                id = report.getId();
                log.info("New task was added");
            } catch (NoSuchElementException e) {
                log.info("Report job, there is no data to work with");
                return;
            }
            this.setProgressStatus(report, Status.PROGRESS, "Request was prepared");
            Mono<List<RecordDto>> resp = this.prepareRequest(report);
            report = this.reportService.get(id);
            List<RecordDto> records = resp.blockOptional().orElseThrow();
            log.info("Data from response was extracted");
            this.saveRecordAsFile(report, records);
        } catch (Exception e) {
            log.error(e.getMessage());
            report.setStatus(Status.ERROR);
            this.reportService.save(report);
        }
    }

    private void saveRecordAsFile(Report report, List<RecordDto> records) throws IOException {
        byte[] convertedFile = xlsxRecordService.getRecordXlsData(recordMapper.listInputMapping(records));
        report.setFileValue(convertedFile);
        this.setProgressStatus(report, Status.DONE, "Report Job finished");
    }

    private Mono<List<RecordDto>> prepareRequest(Report report) {
        WebClient client = WebClient.create(JOB_IMPORT_URI);
        Mono<List<RecordDto>> resp = client.get()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(TOKEN_HEADER, jwtSecret)
                .header("from", report.getParams().getStart().toString())
                .header("to", report.getParams().getFinish().toString())
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
                });
        log.info("Response was returned");
        return resp;
    }

    private void setProgressStatus(Report report, Status progress, String logInfo) {
        report.setStatus(progress);
        this.reportService.save(report);
        log.info(logInfo);
    }
}
