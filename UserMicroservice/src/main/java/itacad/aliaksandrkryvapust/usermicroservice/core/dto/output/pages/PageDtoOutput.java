package itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder
@Data
public class PageDtoOutput<TYPE> {
    private final @NonNull Integer number;
    private final @NonNull Integer size;
    private final @NonNull Integer totalPages;
    private final @NonNull Long totalElements;
    private final @NonNull Boolean first;
    private final @NonNull Integer numberOfElements;
    private final @NonNull Boolean last;
    private final @NonNull List<TYPE> content;
}
