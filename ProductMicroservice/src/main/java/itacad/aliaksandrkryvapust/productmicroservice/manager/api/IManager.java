package itacad.aliaksandrkryvapust.productmicroservice.manager.api;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IManager<TYPE, TYPE2> {
    TYPE save(TYPE2 type);

    PageDtoOutput<TYPE> get(Pageable pageable);
    TYPE get(UUID id);
}
