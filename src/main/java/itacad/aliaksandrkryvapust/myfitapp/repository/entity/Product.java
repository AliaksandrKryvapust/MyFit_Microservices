package itacad.aliaksandrkryvapust.myfitapp.repository.entity;

import lombok.*;

import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
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
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
//    @Type(type="pg-uuid")
    private UUID id;
    @OneToMany(mappedBy = "product")
    @Setter
    private List<Ingredient> ingredients;
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
    @Setter
    private Double weight;
    @Column(updatable = false)
    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    private Instant dtCreate;
    @Version
    private Instant dtUpdate;
}
