package itacad.aliaksandrkryvapust.myfitapp.manager.api;

import java.util.List;
import java.util.UUID;

public interface IManager <TYPE,TYPE2> {
    TYPE save(TYPE2 type);
    List<TYPE> get();
    TYPE get(UUID id);
}
