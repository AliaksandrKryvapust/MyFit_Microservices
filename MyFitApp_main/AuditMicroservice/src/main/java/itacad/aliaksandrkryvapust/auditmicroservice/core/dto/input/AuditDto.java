package itacad.aliaksandrkryvapust.auditmicroservice.core.dto.input;

import itacad.aliaksandrkryvapust.auditmicroservice.controller.validator.api.IValidEnum;
import itacad.aliaksandrkryvapust.auditmicroservice.repository.entity.EType;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
@Jacksonized
public class AuditDto {
    @NotNull(message = "id cannot be null")
    @Size(min = 2, max = 100, message = "id should contain from 2 to 100 letters")
    private final String id;
    @Valid
    private final UserDto user;
    @NotNull(message = "text cannot be null")
    @Size(min = 2, max = 100, message = "text should contain from 2 to 100 letters")
    private final String text;
    @NotNull(message = "type cannot be null")
    @IValidEnum(enumClass = EType.class, message = "type does not match")
    private final String type;
}
