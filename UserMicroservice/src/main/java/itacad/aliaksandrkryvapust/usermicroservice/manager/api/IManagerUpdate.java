package itacad.aliaksandrkryvapust.usermicroservice.manager.api;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public interface IManagerUpdate <TYPE, TYPE2> {
    TYPE update(TYPE2 type, UUID id, Long version, HttpServletRequest request);
}
