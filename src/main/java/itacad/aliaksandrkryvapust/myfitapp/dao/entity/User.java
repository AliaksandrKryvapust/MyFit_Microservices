package itacad.aliaksandrkryvapust.myfitapp.dao.entity;

import lombok.*;

import javax.persistence.*;

import static itacad.aliaksandrkryvapust.myfitapp.core.Constants.MENU_ENTITY_GRAPH;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@NamedEntityGraph(name = MENU_ENTITY_GRAPH, attributeNodes = @NamedAttributeNode("items"))
@Table(name = "users", schema = "app")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @OneToMany
//    @JoinColumn(name = "menu_id", referencedColumnName = "id")
//    @Setter
//    private List<MenuItem> items;
    @Setter
    private String userName;
    @Setter
    private String password;
//    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
//    private Instant creationDate;
//    @Version
//    private Integer version;
}
