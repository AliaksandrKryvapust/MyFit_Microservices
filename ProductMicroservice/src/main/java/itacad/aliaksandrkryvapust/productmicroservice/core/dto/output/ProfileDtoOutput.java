package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EActivityType;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EProfileSex;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class ProfileDtoOutput {
    private final @NonNull String id;
    private final @NonNull Integer height;
    private final @NonNull Double weight;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final @NonNull LocalDate dtBirthday;
    private final @NonNull Double target;
    private final @NonNull String activityType;
    private final @NonNull String sex;
    private final @NonNull UserDtoOutput user;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
