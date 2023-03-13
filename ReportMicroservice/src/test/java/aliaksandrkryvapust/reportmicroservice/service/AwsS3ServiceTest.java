package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.core.aws.AwsS3FileDto;
import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EFileType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EStatus;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Params;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IAwsS3Validator;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AwsS3ServiceTest {
    @InjectMocks
    private AwsS3Service awsS3Service;
    @Mock
    private AmazonS3 amazonS3;
    @Mock
    private IAwsS3Validator awsS3Validator;

    // preconditions
    final URL awsUrl;
    final String fileName = "e6b8e68f-1457-451f-a4ff-5bb65212c8b6";
    final LocalDate from = LocalDate.parse("2022-12-21");
    final LocalDate to = LocalDate.parse("2023-01-12");
    final Instant dtCreate = Instant.ofEpochSecond(1673532204657L);
    final Instant dtUpdate = Instant.ofEpochSecond(1673532532870L);
    final String description = "description";
    final UUID id = UUID.fromString("1d63d7df-f1b3-4e92-95a3-6c7efad96901");
    final EFileType fileType = EFileType.JOURNAL_REPORT;


    {
        try {
            awsUrl = new URL("https://javatests3111222.s3.eu-central-1.amazonaws.com/CV+java+junior+Aliaksandr+Kryvapust.pdf");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void sendFileFromReport() {
        // preconditions
        final byte[] array = new byte[0];
        final Report report = getPreparedReportOutput();
        Mockito.when(amazonS3.generatePresignedUrl(anyString(), anyString(), any(Date.class),
                any(HttpMethod.class))).thenReturn(awsUrl);

        //test
        AwsS3FileDto actual = awsS3Service.sendFileFromReport(array, report, fileType);
        Mockito.verify(amazonS3, Mockito.times(1)).putObject(anyString(), anyString(),
                any(InputStream.class), any(ObjectMetadata.class));
        Mockito.verify(awsS3Validator, Mockito.times(1)).validateFileKeyToStorage(anyString(), anyString());

        // assert
        assertEquals(awsUrl.toString(), actual.getUrl());
    }

    @Test
    void generateUrl() {
        // preconditions
        Mockito.when(amazonS3.generatePresignedUrl(anyString(), anyString(), any(Date.class),
                any(HttpMethod.class))).thenReturn(awsUrl);

        //test
        String actual = awsS3Service.generateUrl(fileName);
        Mockito.verify(awsS3Validator, Mockito.times(1)).validateFileKeyToStorage(anyString(), anyString());

        // assert
        assertEquals(awsUrl.toString(), actual);
    }

    private Params getPreparedParamsOutput() {
        return Params.builder()
                .start(from)
                .finish(to)
                .build();
    }

    private Report getPreparedReportOutput() {
        return Report.builder()
                .dtCreate(dtCreate)
                .dtUpdate(dtUpdate)
                .id(id)
                .status(EStatus.LOADED)
                .type(EType.JOURNAL_FOOD)
                .description(description)
                .params(getPreparedParamsOutput())
                .build();
    }


}