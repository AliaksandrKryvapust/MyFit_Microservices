package itacad.aliaksandrkryvapust.usermicroservice.manager.api;

import itacad.aliaksandrkryvapust.usermicroservice.core.dto.output.pages.PageDtoOutput;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface IManager<TYPE, TYPE2> {
    TYPE save(TYPE2 type, HttpServletRequest request);
    PageDtoOutput get(Pageable pageable);
    TYPE get(UUID id);
}
