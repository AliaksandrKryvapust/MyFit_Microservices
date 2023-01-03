package itacad.aliaksandrkryvapust.myfitapp.repository.api;

import itacad.aliaksandrkryvapust.myfitapp.repository.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface IRecordRepository extends JpaRepository<Record, UUID> {
    @Query("SELECT rec FROM Record rec WHERE rec.dtCreate>=:from AND rec.dtCreate<=:to ORDER BY rec.id")
    List<Record> getRecordByTimeGap(@Param("from") Instant start, @Param("to") Instant end);
}
