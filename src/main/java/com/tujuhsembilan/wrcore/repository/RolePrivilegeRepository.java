package com.tujuhsembilan.wrcore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.RolePrivilege;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

  List<RolePrivilege> findByRolePrivilegeId(Long rolePrivilegeId);



}
