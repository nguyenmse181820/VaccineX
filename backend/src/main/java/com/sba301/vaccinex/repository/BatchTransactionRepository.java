package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.BatchTransaction;
import com.sba301.vaccinex.pojo.composite.BatchTransactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchTransactionRepository extends JpaRepository<BatchTransaction, BatchTransactionId> {

}
