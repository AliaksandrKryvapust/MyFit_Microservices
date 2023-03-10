package aliaksandrkryvapust.reportmicroservice.repository.entity;

import aliaksandrkryvapust.reportmicroservice.core.dto.output.microservices.EType;
import lombok.*;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "report", schema = "report")
public class Report {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Setter
    private EStatus status;
    @Enumerated(EnumType.STRING)
    @Setter
    private EType type;
    @Setter
    private String description;
    @Setter
    @Embedded
    private Params params;
    @Setter
    @Embedded
    private User user;
    @Setter
    @Embedded
    private File file;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", status=" + status +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", params=" + params +
                ", user=" + user +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                '}';
    }
}
