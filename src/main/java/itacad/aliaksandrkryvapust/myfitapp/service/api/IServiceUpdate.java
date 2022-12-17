package itacad.aliaksandrkryvapust.myfitapp.service.api;

import java.time.Instant;
import java.util.UUID;

public interface IServiceUpdate<TYPE> {
    TYPE update(TYPE type, UUID id, Instant version);
}
