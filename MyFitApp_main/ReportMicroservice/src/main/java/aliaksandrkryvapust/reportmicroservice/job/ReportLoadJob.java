package aliaksandrkryvapust.reportmicroservice.job;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.RecordDto;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Status;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.JOB_IMPORT_URI;
import static aliaksandrkryvapust.reportmicroservice.core.Constants.TOKEN_HEADER;

@Slf4j
@Component
public class ReportLoadJob implements Job {
    private static final String jwtSecret = "NDQ1ZjAzNjQtMzViZi00MDRjLTljZjQtNjNjYWIyZTU5ZDYw";
    private final IReportService reportService;

    public ReportLoadJob(IReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("Report Job started");
        Report report;
        try {
            report = this.reportService.getReport(Status.LOADED, Type.JOURNAL_FOOD).orElseThrow();
            log.info("New task was added");
        } catch (NoSuchElementException e) {
            log.info("Report job, there is no data to work with");
            return;
        }
        report.setStatus(Status.PROGRESS);
        this.reportService.save(report);
        log.info("Request was prepared");
        WebClient client = WebClient.create(JOB_IMPORT_URI);
        Mono<List<RecordDto>> resp = client.get()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(TOKEN_HEADER, jwtSecret)
                .header("from",report.getParams().getStart().toString())
                .header("to", report.getParams().getFinish().toString())
                .retrieve().bodyToMono(new ParameterizedTypeReference<>() {
                });
        log.info("Response was returned");
        try {
            List<RecordDto> records = resp.blockOptional().orElseThrow();
            log.info("Data from response was extracted");
        } catch (NoSuchElementException e) {
            log.error("Data from response was corrupted");
            report.setStatus(Status.ERROR);
            this.reportService.save(report);
        }
        // TODO map list to a file
        report.setStatus(Status.DONE);
        this.reportService.save(report);
        log.info("Report Job finished");
    }
}
