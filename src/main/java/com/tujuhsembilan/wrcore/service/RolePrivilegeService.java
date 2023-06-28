package com.tujuhsembilan.wrcore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.model.RolePrivilege;
import com.tujuhsembilan.wrcore.repository.RolePrivilegeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePrivilegeService {
  private final RolePrivilegeRepository rolePrivilegeRepository;

  public void deleteRolePrivilege(Long rolePrivilegeId) throws NotFoundException {
    if (!rolePrivilegeRepository.existsById(rolePrivilegeId)) {
      throw new NotFoundException();
    }
    rolePrivilegeRepository.deleteById(rolePrivilegeId);
  }

  public Page<RolePrivilege> getAllRolePrivilege(Pageable pageable) {
    return rolePrivilegeRepository.findAll(pageable);
  }

  public List<RolePrivilege> getRolePrivilegeById(Long rolePrivilege){
    return rolePrivilegeRepository.findByRolePrivilegeId(rolePrivilege);
  }

  // public RolePrivilege addRolePrivilege(RolePrivilegeDTO rolePrivilegeDto) {
  // RolePrivilege rolePrivilege =
  // RolePrivilege.builder().roleId(rolePrivilegeDto.getRoleId())
  // .privilegeId(rolePrivilegeDto.getPrivilegeId()).build();
  // rolePrivilegeRepository.save(rolePrivilege);
  // return rolePrivilege;
  // }

  // public RolePrivilege updateRolePrivilege(Long id, RolePrivilegeDTO
  // rolePrivilegeDto) {
  // Optional<RolePrivilege> rolePrivile = rolePrivilegeRepository.findById(id);
  // if (rolePrivile.isPresent()) {
  // rolePrivile.get().setRoleId(rolePrivilegeDto.getRoleId());
  // rolePrivile.get().setPrivilegeId(rolePrivilegeDto.getPrivilegeId());
  // rolePrivilegeRepository.save(rolePrivile.get());
  // return rolePrivile.get();
  // }
  // throw new IllegalArgumentException("Data Not Found");
  // }

}
