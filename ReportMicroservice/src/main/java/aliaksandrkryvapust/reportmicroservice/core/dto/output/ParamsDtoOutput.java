package aliaksandrkryvapust.reportmicroservice.core.dto.output;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Builder
@Data
public class ParamsDtoOutput {
    private final @NonNull LocalDate from;
    private final @NonNull LocalDate to;
}
