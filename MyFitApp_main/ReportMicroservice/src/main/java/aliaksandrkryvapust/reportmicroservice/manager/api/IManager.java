package aliaksandrkryvapust.reportmicroservice.manager.api;

import aliaksandrkryvapust.reportmicroservice.core.dto.pages.PageDtoOutput;
import aliaksandrkryvapust.reportmicroservice.repository.entity.Type;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IManager<TYPE, TYPE2> {
    TYPE save(TYPE2 dto, Type type);
    PageDtoOutput get(Pageable pageable);
    TYPE get(UUID id);
}
