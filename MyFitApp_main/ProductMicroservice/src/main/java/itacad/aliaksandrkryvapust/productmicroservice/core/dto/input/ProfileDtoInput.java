package itacad.aliaksandrkryvapust.productmicroservice.core.dto.input;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EActivityType;
import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.EProfileSex;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Builder
@Data
@Jacksonized
public class ProfileDtoInput {
    private final @NonNull Integer height;
    private final @NonNull Double weight;
    private final @NonNull LocalDate dtBirthday;
    private final @NonNull Double target;
    private final @NonNull EActivityType activityType;
    private final @NonNull EProfileSex sex;
}
