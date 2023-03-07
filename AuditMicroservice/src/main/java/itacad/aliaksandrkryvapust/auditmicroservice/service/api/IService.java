package itacad.aliaksandrkryvapust.auditmicroservice.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IService<TYPE> {
    void save(TYPE type);

    Page<TYPE> get(Pageable pageable);
}
