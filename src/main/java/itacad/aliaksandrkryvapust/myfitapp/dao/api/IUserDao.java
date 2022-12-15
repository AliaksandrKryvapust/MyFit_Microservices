package itacad.aliaksandrkryvapust.myfitapp.dao.api;

import itacad.aliaksandrkryvapust.myfitapp.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserDao extends JpaRepository<User, Long> {
//    @EntityGraph(value = MENU_ENTITY_GRAPH)
//    @NonNull Optional<User> findById(@NonNull Long id);
}

