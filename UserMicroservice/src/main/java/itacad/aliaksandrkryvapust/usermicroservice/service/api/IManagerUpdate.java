package itacad.aliaksandrkryvapust.usermicroservice.service.api;

import java.util.UUID;

public interface IManagerUpdate<TYPE, TYPE2> {
    TYPE updateDto(TYPE2 type, UUID id, Long version);
}
