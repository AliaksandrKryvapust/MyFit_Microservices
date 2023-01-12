package aliaksandrkryvapust.reportmicroservice.core.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class ParamsDtoOutput {
    private final @NonNull LocalDate from;
    private final @NonNull LocalDate to;
}
