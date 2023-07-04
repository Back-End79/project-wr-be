package com.tujuhsembilan.wrcore.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.RolePrivilegeDTO;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.RolePrivilege;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.RolePrivilegeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePrivilegeService {
  private final RolePrivilegeRepository rolePrivilegeRepository;
  private final CategoryCodeRepository categoryCodeRepository;

  public void deleteRolePrivilege(Long rolePrivilegeId) throws NotFoundException {
    if (!rolePrivilegeRepository.existsById(rolePrivilegeId)) {
      throw new NotFoundException();
    }
    rolePrivilegeRepository.deleteById(rolePrivilegeId);
  }

  public Page<RolePrivilege> getAllRolePrivilege(Pageable pageable) {
    return rolePrivilegeRepository.findAll(pageable);
  }

  public Page<RolePrivilege> getRoleById(String rolePrivilege, Pageable pageable) {
    return rolePrivilegeRepository.findByRoleId(rolePrivilege, pageable);
  }

  public RolePrivilege getRolePrivilegeId(Long rolePrivilegeId) throws NotFoundException {
    RolePrivilege rolePrivilege = rolePrivilegeRepository.findByRolePrivilegeId(rolePrivilegeId).orElseThrow(() -> new NotFoundException());
    return rolePrivilege;
  }

  public RolePrivilege createRolePrivilege(RolePrivilegeDTO rolePrivilegeDTO) throws NotFoundException {
    RolePrivilege rolePrivilege = createRolePrivilegeMapper(rolePrivilegeDTO);
    rolePrivilegeRepository.save(rolePrivilege);
    return rolePrivilege;
  }

  public RolePrivilege updateRolePrivilege(Long id, RolePrivilegeDTO rolePrivilegeDto) throws NotFoundException {
    
    RolePrivilege rolePrivilege = getRolePrivilegeId(id);
    CategoryCode roleId = getCategoryCodeById(rolePrivilegeDto.getRoleId());
    CategoryCode privilegeId = getCategoryCodeById(rolePrivilegeDto.getPrivilegeId());
    
    RolePrivilege updateRolePrivilege = updateRolePrivilegeMapper(rolePrivilege, rolePrivilegeDto, roleId, privilegeId);
    
    RolePrivilege savedRolePrivilege = rolePrivilegeRepository.save(updateRolePrivilege);
    return savedRolePrivilege;
  }

  private CategoryCode getCategoryCodeById(Long categoryId) throws NotFoundException {
    return categoryCodeRepository.findByCategoryCodeId(categoryId)
        .orElseGet(() -> new CategoryCode());
  }
  
  private RolePrivilege createRolePrivilegeMapper(RolePrivilegeDTO rolePrivilegeDto) throws NotFoundException {
    RolePrivilege rolePrivilege =
    RolePrivilege.builder()
                  .roleId(categoryCodeRepository.findById(rolePrivilegeDto.getRoleId()).orElseThrow(() -> new NotFoundException()))
                  .privilegeId(categoryCodeRepository.findById(rolePrivilegeDto.getPrivilegeId()).orElseThrow(() -> new NotFoundException()))
                  .build();
    rolePrivilegeRepository.save(rolePrivilege);
  return rolePrivilege;
  }
  
  private RolePrivilege updateRolePrivilegeMapper(RolePrivilege rolePrivilege, RolePrivilegeDTO rolePrivilegeDTO,
      CategoryCode roleId, CategoryCode privilegeId) {
    return RolePrivilege.builder()
        .rolePrivilegeId(rolePrivilege.getRolePrivilegeId())
        .roleId(roleId)
        .privilegeId(privilegeId)
        .build();
  }
}
