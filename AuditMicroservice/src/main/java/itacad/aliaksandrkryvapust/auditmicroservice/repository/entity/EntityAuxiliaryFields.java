package itacad.aliaksandrkryvapust.auditmicroservice.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public abstract class EntityAuxiliaryFields {
    @Id
    protected UUID id;
    protected Instant dtCreate;
}
