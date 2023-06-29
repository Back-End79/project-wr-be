package com.tujuhsembilan.wrcore.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.RolePrivilege;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

  List<RolePrivilege> findByRoleId(Long roleId);

  Optional<RolePrivilege> findByRolePrivilegeId(Long rolePrivilegeId);


}
