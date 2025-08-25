package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Combo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Integer>, JpaSpecificationExecutor<Combo> {
    Page<Combo> findAllByDeletedFalse(Pageable pageable);

    Page<Combo> findByDeletedIsFalse(Pageable pageable);

    Page<Combo> findAll(Specification<Combo> spec, Pageable pageable);

    Optional<Combo> findByIdAndDeletedIsFalse(Integer id);

    Combo findComboByName(String name);

    Combo findComboById(int id);

    @Query(value = "select max(price) from Combo")
    Double getMaxPrice();

    @Query(value = "select max(maxAge) from Combo")
    Integer getMaxAge();

}