package aliaksandrkryvapust.reportmicroservice.repository.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Params {
    private Instant from;
    private Instant to;
}
