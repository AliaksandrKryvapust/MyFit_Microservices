package aliaksandrkryvapust.reportmicroservice.core.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@Jacksonized
public class ParamsDto {
    @NotNull(message = "start date cannot be null")
    @Past(message = "start date should refer to moment in past")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDate from;
    @NotNull(message = "end date cannot be null")
    @PastOrPresent(message = "end date should refer to moment in past")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private final LocalDate to;
    @NotNull(message = "userId cannot be null")
    @Size(min = 10, max = 100, message = "userId should contain from 2 to 100 letters")
    private final UUID userId;
}
