package itacad.aliaksandrkryvapust.productmicroservice.repository.api;

import itacad.aliaksandrkryvapust.productmicroservice.repository.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRecordRepository extends JpaRepository<Record, UUID> {
    Optional<Record> findByIdAndUserId(UUID uuid, UUID userId);
    Page<Record> findAllByUserId(Pageable pageable, UUID userId);
    @Query("SELECT rec FROM Record rec WHERE rec.dtCreate>=:from AND rec.dtCreate<=:to ORDER BY rec.id")
    List<Record> getRecordByTimeGap(@Param("from") Instant start, @Param("to") Instant end);
}
