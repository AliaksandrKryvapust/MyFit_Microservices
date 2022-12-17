package itacad.aliaksandrkryvapust.myfitapp.manager.api;

import java.util.List;

public interface IManager <TYPE,TYPE2> {
    TYPE save(TYPE2 type);
    List<TYPE> get();
    TYPE get(Long id);
}
