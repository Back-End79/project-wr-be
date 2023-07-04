package com.tujuhsembilan.wrcore.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tujuhsembilan.wrcore.model.RolePrivilege;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {

  Page<RolePrivilege> findByRoleId(String roleId, Pageable pageable);

  Optional<RolePrivilege> findByRolePrivilegeId(Long rolePrivilegeId);


}
