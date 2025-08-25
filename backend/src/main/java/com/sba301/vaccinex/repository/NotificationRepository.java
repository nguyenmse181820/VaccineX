package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByScheduleCustomerIdOrderByDateDesc(Integer id);
}
