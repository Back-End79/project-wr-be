package com.tujuhsembilan.wrcore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.RolePrivilege;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

  Optional<RolePrivilege> findByRolePrivilegeId(Long rolePrivilegeId);

}
