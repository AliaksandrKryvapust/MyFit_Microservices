package itacad.aliaksandrkryvapust.auditmicroservice.repository.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@Document
public class Audit extends EntityAuxiliaryFields {
    private UUID actionId;
    private User user;
    private String text;
    private EType type;
}
