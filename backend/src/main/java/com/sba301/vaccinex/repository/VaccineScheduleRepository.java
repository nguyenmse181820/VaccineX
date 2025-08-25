package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.User;
import com.sba301.vaccinex.pojo.Vaccine;
import com.sba301.vaccinex.pojo.VaccineSchedule;
import com.sba301.vaccinex.pojo.enums.VaccineScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VaccineScheduleRepository extends JpaRepository<VaccineSchedule, Integer> {

    List<VaccineSchedule> findByDeletedIsFalseAndStatus(VaccineScheduleStatus status);

    List<VaccineSchedule> findByChildIdAndVaccineIdOrderByDateAsc(Integer childId, Integer vaccineId);

    void deleteByStatusAndChildId(VaccineScheduleStatus status, Integer childId);

    List<VaccineSchedule> findByStatusAndChildId(VaccineScheduleStatus status, Integer childId);

    List<VaccineSchedule> findByChildIdAndDateAfterOrderByDateAsc(Integer childId, LocalDateTime date);

    Page<VaccineSchedule> findByCustomer(User customer, Pageable pageable);

    boolean existsByDoctorAndDate(User doctor, LocalDateTime date);

    List<VaccineSchedule> findByDeletedIsFalseAndStatusAndDateIsBetweenOrderByDateAsc(VaccineScheduleStatus status, LocalDateTime dateAfter, LocalDateTime dateBefore);

    @Query("SELECT COUNT(s) FROM VaccineSchedule s WHERE s.batch.id = ?1 AND s.status = 'PLANNED'")
    int countBatch(int batchId);

    List<VaccineSchedule> findByChildIdOrderByDateDesc(Integer childId);

    List<VaccineSchedule> findByDoctorId(Integer doctorId);

    Integer vaccine(Vaccine vaccine);
}