package aliaksandrkryvapust.reportmicroservice.repository.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Params {
    private LocalDate start;
    private LocalDate finish;
}
