package aliaksandrkryvapust.reportmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class FileDtoOutput {
    private final @NonNull String fileType;
    private final @NonNull String contentType;
    private final @NonNull String fileName;
    private final @NonNull String url;
}
