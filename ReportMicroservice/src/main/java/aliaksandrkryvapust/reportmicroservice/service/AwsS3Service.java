package aliaksandrkryvapust.reportmicroservice.service;

import aliaksandrkryvapust.reportmicroservice.core.aws.AwsS3FileDto;
import aliaksandrkryvapust.reportmicroservice.core.aws.FileMetadata;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EFileType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;
import aliaksandrkryvapust.reportmicroservice.service.api.IAwsS3Service;
import aliaksandrkryvapust.reportmicroservice.service.validator.api.IAwsS3Validator;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static aliaksandrkryvapust.reportmicroservice.core.Constants.BUCKET_NAME;
import static aliaksandrkryvapust.reportmicroservice.core.Constants.XLSX_CONTENT_TYPE;

@Service
@RequiredArgsConstructor
public class AwsS3Service implements IAwsS3Service {
    private final AmazonS3 amazonS3;
    private final IAwsS3Validator awsS3Validator;

    public AwsS3FileDto sendFileFromReport(byte[] convertedFile, Report report, EFileType fileType) {
        if (fileType.equals(EFileType.JOURNAL_REPORT)) {
            String awsFileName = report.getId() + "." + XLSX_CONTENT_TYPE.substring(XLSX_CONTENT_TYPE.indexOf("/") + 1);
            FileMetadata metadata = new FileMetadata(Objects.requireNonNull(XLSX_CONTENT_TYPE), convertedFile.length);
            amazonS3.putObject(BUCKET_NAME, awsFileName, new ByteArrayInputStream(convertedFile), metadata);
            String url = generateUrl(awsFileName);
            return new AwsS3FileDto(url, awsFileName);
        } else {
            throw new IllegalStateException("Unsupported file type");
        }
    }

    @Override
    public String generateUrl(String awsFileName) {
        Calendar calendar = generateExpirationTime();
        awsS3Validator.validateFileKeyToStorage(BUCKET_NAME, awsFileName);
        return amazonS3.generatePresignedUrl(BUCKET_NAME, awsFileName, calendar.getTime(), HttpMethod.GET).toString();
    }

    @NonNull
    private Calendar generateExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 10); // Expiration time 10 days
        return calendar;
    }
}
