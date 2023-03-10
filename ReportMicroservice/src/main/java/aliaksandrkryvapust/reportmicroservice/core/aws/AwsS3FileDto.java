package aliaksandrkryvapust.reportmicroservice.core.aws;

import lombok.Data;
import lombok.NonNull;

@Data
public class AwsS3FileDto {
    private final @NonNull String url;
    private final @NonNull String fileKey;
}
