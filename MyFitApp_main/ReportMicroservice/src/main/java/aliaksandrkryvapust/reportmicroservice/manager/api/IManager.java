package aliaksandrkryvapust.reportmicroservice.manager.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IManager<TYPE, TYPE2> {
    TYPE save(TYPE2 type);
    PageDtoOutput get(Pageable pageable);
    TYPE get(UUID id);
}
