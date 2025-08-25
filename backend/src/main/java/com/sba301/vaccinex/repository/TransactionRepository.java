package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Transaction;
import com.sba301.vaccinex.pojo.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByIdAndDeletedIsFalse(Integer id);
    List<Transaction> findByDoctorId(Integer doctorId);
    List<Transaction> findByDoctorIdAndDateAfterAndDateBefore(Integer doctorId, LocalDateTime dateAfter, LocalDateTime dateBefore);
}
