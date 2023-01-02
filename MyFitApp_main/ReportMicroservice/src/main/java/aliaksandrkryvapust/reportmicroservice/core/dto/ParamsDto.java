package aliaksandrkryvapust.reportmicroservice.core.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.Instant;

@Builder
@Data
@Jacksonized
public class ParamsDto {
    @NotNull(message = "start date cannot be null")
    @Past(message = "start date should refer to moment in past")
    private final Instant from;
    @NotNull(message = "end date cannot be null")
    @Past(message = "end date should refer to moment in past")
    private final Instant to;
}
