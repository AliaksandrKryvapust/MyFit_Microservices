package itacad.aliaksandrkryvapust.productmicroservice.service.api;

import itacad.aliaksandrkryvapust.productmicroservice.core.dto.output.pages.PageDtoOutput;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IManager<TYPE, TYPE2> {
    TYPE saveDto(TYPE2 type);

    PageDtoOutput<TYPE> getDto(Pageable pageable);
    TYPE getDto(UUID id);
}
