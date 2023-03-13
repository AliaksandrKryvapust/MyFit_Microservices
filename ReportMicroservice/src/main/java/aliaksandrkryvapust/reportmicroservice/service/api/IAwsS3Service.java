package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.aws.AwsS3FileDto;
import aliaksandrkryvapust.reportmicroservice.repository.entity.EFileType;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Report;

public interface IAwsS3Service {
    String generateUrl(String fileName);

    AwsS3FileDto sendFileFromReport(byte[] convertedFile, Report report, EFileType fileType);
}
