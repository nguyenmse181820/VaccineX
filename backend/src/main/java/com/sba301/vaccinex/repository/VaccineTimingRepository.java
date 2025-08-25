package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Vaccine;
import com.sba301.vaccinex.pojo.VaccineTiming;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccineTimingRepository extends JpaRepository<VaccineTiming, Integer> {
    Optional<VaccineTiming> findByIdAndDeletedIsFalse(Integer id);
    Optional<VaccineTiming> findByVaccineAndDoseNoAndDeletedIsFalse(Vaccine vaccine, int doseNo);
}
