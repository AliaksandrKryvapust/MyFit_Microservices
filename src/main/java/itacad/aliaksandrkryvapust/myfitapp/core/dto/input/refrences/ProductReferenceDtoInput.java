package itacad.aliaksandrkryvapust.myfitapp.core.dto.input.refrences;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Builder
@Data
@Jacksonized
public class ProductReferenceDtoInput {
    @NotNull(message = "product cannot be empty")
    private final UUID uuid;
}
