package itacad.aliaksandrkryvapust.myfitapp.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IService<TYPE> {
    TYPE save(TYPE type);
    Page<TYPE> get(Pageable pageable);
    TYPE get(UUID id);
}
