package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Vaccine;
import com.sba301.vaccinex.pojo.VaccineInterval;
import com.sba301.vaccinex.pojo.composite.VaccineIntervalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VaccineIntervalRepository extends JpaRepository<VaccineInterval, VaccineIntervalId> {
    List<VaccineInterval> findByToVaccine(Vaccine toVaccine);

    @Transactional
    @Modifying
    @Query("DELETE FROM VaccineInterval v WHERE v.fromVaccine.id = :fromVaccineId")
    void deleteByFromVaccineId(@Param("fromVaccineId") int fromVaccineId);

}
