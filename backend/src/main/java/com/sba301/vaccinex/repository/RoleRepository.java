package com.sba301.vaccinex.repository;

import com.sba301.vaccinex.pojo.Role;
import com.sba301.vaccinex.pojo.enums.EnumRoleNameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleNameAndDeletedIsFalse(EnumRoleNameType name);
    Optional<Role> findByIdAndDeletedIsFalse(Integer id);
    List<Role> findAllByDeletedIsFalse();
    Role getRoleByRoleName(EnumRoleNameType role);
}
