package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Combo;
import com.sba301.vaccinex.pojo.VaccineCombo;
import com.sba301.vaccinex.pojo.composite.VaccineComboId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineComboRepository extends JpaRepository<VaccineCombo, VaccineComboId> {

    List<VaccineCombo> getVaccineCombosByCombo(Combo combo);

}
