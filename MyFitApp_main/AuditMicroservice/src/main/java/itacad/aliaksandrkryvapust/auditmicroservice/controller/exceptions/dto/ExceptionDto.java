package itacad.aliaksandrkryvapust.auditmicroservice.controller.exceptions.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class ExceptionDto {
    private final @NonNull String field;
    private final @NonNull String message;
}

