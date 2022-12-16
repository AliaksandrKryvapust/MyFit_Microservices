package itacad.aliaksandrkryvapust.myfitapp.repository.entity;

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
@Table(name = "products", schema = "app")
public class Product {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
//    @Type(type="pg-uuid")
    private UUID id;
    @Setter
    private String title;
    @Setter
    private Integer calories;
    @Setter
    private Integer proteins;
    @Setter
    private Integer fats;
    @Setter
    private Integer carbohydrates;
    @Column(updatable = false)
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;
}
