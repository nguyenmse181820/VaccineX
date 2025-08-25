package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.VaccineUse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineUseRepository extends JpaRepository<VaccineUse, Integer>, JpaSpecificationExecutor<VaccineUse> {
    Page<VaccineUse> findAllByDeletedFalse(Pageable pageable);

    VaccineUse findPurposeById(int id);

    VaccineUse findPurposeByName(String name);

    List<VaccineUse> findByDeletedIsFalse();

    List<VaccineUse> findAllByIdInAndDeletedFalse(List<Integer> vaccineUseIds);

}
