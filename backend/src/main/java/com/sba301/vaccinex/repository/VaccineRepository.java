package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Integer>, JpaSpecificationExecutor<Vaccine> {

    Page<Vaccine> findAllByDeletedFalse(Pageable pageable);

    Vaccine findVaccineByVaccineCode(String vaccineCode);

    Vaccine findVaccineById(int id);

    Optional<Vaccine> findByIdAndDeletedIsFalse(Integer id); //AnhNT

    Page<Vaccine> findAll(Specification<Vaccine> spec, Pageable pageable);

    @Query(value = "select max(price) from Vaccine")
    Double getMaxPrice();

    @Query(value = "select max(maxAge) from Vaccine")
    Integer getMaxAge();

    List<Vaccine> findByDeletedIsFalse();
}
