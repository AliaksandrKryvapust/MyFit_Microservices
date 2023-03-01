package itacad.aliaksandrkryvapust.productmicroservice.repository.entity;

import lombok.*;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profile", schema = "app")
public class Profile {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Setter
    private Integer height;
    @Setter
    private Double weight;
    @Setter
    private LocalDate dtBirthday;
    @Setter
    private Double target;
    @Enumerated(EnumType.STRING)
    @Setter
    private EActivityType activityType;
    @Enumerated(EnumType.STRING)
    @Setter
    private EProfileSex sex;
    @Setter
    @Embedded
    private User user;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", height=" + height +
                ", weight=" + weight +
                ", dtBirthday=" + dtBirthday +
                ", target=" + target +
                ", activityType=" + activityType +
                ", sex=" + sex +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                '}';
    }
}
