package itacad.aliaksandrkryvapust.auditmicroservice.service.api;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import org.springframework.data.domain.Pageable;

public interface IManager<TYPE, TYPE2> {
    void saveDto(TYPE type);

    PageDtoOutput<TYPE2> getDto(Pageable pageable);
}
