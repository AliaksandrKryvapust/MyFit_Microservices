package aliaksandrkryvapust.reportmicroservice.repository.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
    private String username;
    @Basic(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @org.hibernate.annotations.Type(type="org.hibernate.type.BinaryType")
    @Setter
    private byte[] fileValue;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;

    public Report(byte[] fileValue) {
        this.fileValue = fileValue;
    }
}
