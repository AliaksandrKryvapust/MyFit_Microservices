package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IProfileRepository extends JpaRepository<Profile, UUID> {
    @Transactional(readOnly = true)
    Page<Profile> findAllByUser_UserId(Pageable pageable, UUID userId);
    @Transactional(readOnly = true)
    Optional<Profile> findByIdAndUser_UserId(UUID id, UUID userId);
}
