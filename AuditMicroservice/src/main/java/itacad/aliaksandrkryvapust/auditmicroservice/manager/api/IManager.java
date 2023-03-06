package itacad.aliaksandrkryvapust.auditmicroservice.manager.api;

import itacad.aliaksandrkryvapust.auditmicroservice.core.dto.pages.PageDtoOutput;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IManager<TYPE, TYPE2> {
    TYPE2 save(TYPE type);
    PageDtoOutput<TYPE2> get(Pageable pageable);
    TYPE2 get(UUID id);
}
