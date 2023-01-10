package itacad.aliaksandrkryvapust.productmicroservice.core.dto.output;

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
    private final @NonNull UUID id;
    private final @NonNull Integer height;
    private final @NonNull Double weight;
    private final @NonNull LocalDate dtBirthday;
    private final @NonNull Double target;
    private final @NonNull EActivityType activityType;
    private final @NonNull EProfileSex sex;
    private final @NonNull UserDtoOutput user;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
