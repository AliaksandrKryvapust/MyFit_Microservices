package itacad.aliaksandrkryvapust.myfitapp.manager.api;

import java.util.UUID;

public interface IManagerUpdate <TYPE, TYPE2> {
    TYPE update(TYPE2 type, UUID id, Long version);
}
