package aliaksandrkryvapust.reportmicroservice.core.aws;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FileMetadata extends ObjectMetadata {
    private final @NonNull String contentType;
    private final long contentLength;
}
