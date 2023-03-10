package aliaksandrkryvapust.reportmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class ReportDtoOutput {
    private final @NonNull String id;
    private final @NonNull String status;
    private final @NonNull String type;
    private final @NonNull String description;
    private final @NonNull ParamsDtoOutput params;
    private final @NonNull Long dtCreate;
    private final @NonNull Long dtUpdate;
}
