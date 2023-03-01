package itacad.aliaksandrkryvapust.productmicroservice.service.api;

import java.util.UUID;

public interface IServiceUpdate<TYPE> {
    TYPE update(TYPE type, UUID id, Long version, UUID userId);
}
