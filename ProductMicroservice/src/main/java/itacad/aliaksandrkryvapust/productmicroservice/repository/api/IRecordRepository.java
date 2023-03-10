package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRecordRepository extends JpaRepository<Record, UUID> {
    @Transactional(readOnly = true)
    Optional<Record> findByIdAndUserId(UUID uuid, UUID userId);
    @Transactional(readOnly = true)
    Page<Record> findAllByUserId(Pageable pageable, UUID userId);
    @Transactional(readOnly = true)
    @Query("SELECT rec FROM Record rec WHERE rec.dtCreate>=:from AND rec.dtCreate<=:to AND rec.userId=:id ORDER BY rec.dtCreate")
    List<Record> getRecordByTimeGap(@Param("from") Instant start, @Param("to") Instant end, @Param("id") UUID id);
}
