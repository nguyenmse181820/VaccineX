package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildrenRepository extends JpaRepository<Child, Integer> {

    Optional<Child> findByIdAndDeletedIsFalse(Integer id);

    List<Child> findAllByDeletedIsFalse();

    List<Child>  findAllByGuardianIdAndDeletedIsFalse(Integer guardianId);
}
