package aliaksandrkryvapust.reportmicroservice.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

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
}
