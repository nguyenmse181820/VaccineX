package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Batch;
import com.sba301.vaccinex.pojo.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer>, JpaSpecificationExecutor<Batch> {
    List<Batch> findByDeletedIsFalse();

    Optional<Batch> findByIdAndDeletedIsFalse(Integer id);

    List<Batch> findByVaccineOrderByExpirationAsc(Vaccine vaccine);

//    List<Batch> findByVaccineIdAndExpirationBeforeAndDeletedIsFalseOrderByExpirationAsc(Integer vaccineId, LocalDateTime expiration);

    @Query("SELECT b from Batch b WHERE b.deleted = false and b.vaccine.id = ?1 and b.expiration >= ?2")
    List<Batch> findByVaccineIdAndExpirationBeforeAndDeletedIsFalseOrderByExpirationAsc(Integer vaccineId, LocalDateTime date);

    @Query("SELECT b FROM Batch b " +
            "WHERE b.vaccine.id = :vaccineId " +
            "AND b.expiration > :appointmentDate " +
            "AND b.quantity > 0")
    List<Batch> findByVaccineIdAndExpirationAfter(
            @Param("vaccineId") Integer vaccineId,
            @Param("appointmentDate") LocalDateTime appointmentDate
    );
}
