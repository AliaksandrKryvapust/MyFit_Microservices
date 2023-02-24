package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Data
public class UserDtoOutput {
    private final @NonNull String id;
    private final @NonNull String username;
    private final @NonNull String email;
    private final @NonNull String role;
    private final @NonNull String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private final @Nullable LocalDate dtLogin;
    private final @NonNull Instant dtCreate;
    private final @NonNull Instant dtUpdate;
}
