package aliaksandrkryvapust.reportmicroservice.service.api;

import aliaksandrkryvapust.reportmicroservice.core.security.MyUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IService<TYPE> {
    TYPE save(TYPE type);
    Page<TYPE> get(Pageable pageable, MyUserDetails userDetails);
    TYPE get(UUID id, MyUserDetails userDetails);
}
