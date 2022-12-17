package itacad.aliaksandrkryvapust.myfitapp.service.api;

import java.util.List;
import java.util.UUID;

public interface IService<TYPE> {
    TYPE save(TYPE type);
    List<TYPE> get();
    TYPE get(UUID id);
}
