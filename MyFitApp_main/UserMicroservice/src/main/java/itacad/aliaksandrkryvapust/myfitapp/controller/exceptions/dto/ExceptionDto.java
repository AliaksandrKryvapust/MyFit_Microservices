package itacad.aliaksandrkryvapust.myfitapp.controller.exceptions.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.ObjectError;

import java.util.function.Function;

@Builder
@Data
public class ExceptionDto {
    private final @NonNull String field;
    private final @NonNull String message;
}
