package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Role;
import com.sba301.vaccinex.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByIdAndDeletedIsFalse(Integer id); //AnhNT

    List<User> findByRoleAndDeletedIsFalse(Role role);

    User getAccountByEmail(String email);

    Optional<User> getAccountByEmailAndDeletedIsFalse(String email);

}
