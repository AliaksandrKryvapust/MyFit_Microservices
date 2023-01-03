package itacad.aliaksandrkryvapust.myfitapp.core.dto.export;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Data
public class ParamsDto {
    private final @NonNull Instant from;
    private final @NonNull Instant to;
}
