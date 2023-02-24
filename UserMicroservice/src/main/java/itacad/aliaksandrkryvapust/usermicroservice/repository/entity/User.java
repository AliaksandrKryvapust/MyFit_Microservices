package itacad.aliaksandrkryvapust.usermicroservice.repository.entity;

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
@Table(name = "users", schema = "users")
public class User{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Setter
    @Column(nullable = false, unique = true)
    private String username;
    @Setter
    @Column(nullable = false)
    private String password;
    @Setter
    @Column(nullable = false, unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private EUserRole role;
    @Setter
    @Enumerated(EnumType.STRING)
    private EUserStatus status;
    @Setter
    private LocalDate dtLogin;
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + role +
                ", status=" + status +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                '}';
    }
}
