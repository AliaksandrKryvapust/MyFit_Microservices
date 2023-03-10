package aliaksandrkryvapust.reportmicroservice.job;

import aliaksandrkryvapust.reportmicroservice.core.dto.job.RecordDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.core.dto.poi.XlsxRecord;
import aliaksandrkryvapust.reportmicroservice.core.mapper.poi.XlsxRecordMapper;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.service.api.IReportService;
import aliaksandrkryvapust.reportmicroservice.service.api.IXlsxRecordService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Component
public class ReportLoadJob implements Job {
    private static final String jwtSecret = "NDQ1ZjAzNjQtMzViZi00MDRjLTljZjQtNjNjYWIyZTU5ZDYw";
    private final IReportService reportService;
    private final IXlsxRecordService xlsxRecordService;
    private final XlsxRecordMapper recordMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Report report = new Report();
        try {
            log.info("Report Job started");
            UUID id;
            report = reportService.getReport(EStatus.LOADED, EType.JOURNAL_FOOD)
                    .orElseThrow(NoSuchElementException::new);
            id = report.getId();
            log.info("New task was added");
            setProgressStatus(report, EStatus.PROGRESS, "Request was prepared");
            Mono<List<RecordDto>> resp = prepareRequest(report);
            report = reportService.getWithoutCredentialsCheck(id);
            List<RecordDto> records = resp.blockOptional().orElseThrow(IllegalStateException::new);
            log.info("Data from response was extracted");
            saveRecordAsFile(report, records);
        } catch (NoSuchElementException e) {
            log.info("Report job, there is no data to work with");
        } catch (IllegalStateException e) {
            log.error("Can`t decode data from response");
        } catch (Exception e) {
            log.error(e.getMessage());
            report.setStatus(EStatus.ERROR);
            reportService.save(report);
        }
    }

    private void saveRecordAsFile(Report report, List<RecordDto> records) throws IOException {
        List<XlsxRecord> recordList = recordMapper.listInputMapping(records);
        byte[] convertedFile = xlsxRecordService.getRecordXlsData(recordList);
        report.setFileValue(convertedFile);
        setProgressStatus(report, EStatus.DONE, "Report Job finished");
    }

    private Mono<List<RecordDto>> prepareRequest(Report report) {
        WebClient client = WebClient.create(JOB_IMPORT_URI);
        Mono<List<RecordDto>> resp = client.get()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(TOKEN_HEADER, jwtSecret)
                .header("from", report.getParams().getStart().toString())
                .header("to", report.getParams().getFinish().toString())
                .header("id", report.getUser().getUserId().toString())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
        log.info("Response was returned");
        return resp;
    }

    private void setProgressStatus(Report report, EStatus progress, String logInfo) {
        report.setStatus(progress);
        reportService.save(report);
        log.info(logInfo);
    }
}
