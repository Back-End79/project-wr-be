package com.tujuhsembilan.wrcore.service;

import com.tujuhsembilan.wrcore.dto.*;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.RolePrivilege;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.RolePrivilegeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePrivilegeService {
  private final RolePrivilegeRepository rolePrivilegeRepository;
  private final CategoryCodeRepository categoryCodeRepository;

  @Transactional
  public void deleteRolePrivilege(Long id){
    try {
      CategoryCode role = categoryCodeRepository.findByCategoryCodeId(id).orElseThrow(() -> new EntityNotFoundException("Role Id Not Found"));
      List<RolePrivilege> privilege = rolePrivilegeRepository.findByIdList(role);
      for(RolePrivilege uRole : privilege){
        uRole.setActive(false);
        uRole.setUpdateOn(new Timestamp(System.currentTimeMillis()));
      }
      rolePrivilegeRepository.saveAll(privilege);
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }

  public List<Map<String, Object>> findAllRoleId (Pageable pageable, String search){
      return rolePrivilegeRepository.findAllRoleId(pageable, search);
  }

  public List<DetailRolePrivilegeDTO> convertToRolePrivilegeDTO(Pageable pageable, String search) {
      List<Map<String, Object>> listRoleID = findAllRoleId(pageable, search);
    try {
        List<DetailRolePrivilegeDTO> listResult = new ArrayList<>();
        for (Map<String, Object> dri: listRoleID) {
            String test = Objects.toString(dri.get("role_id"));
            Integer roleIdInt =  Integer.parseInt(test);
            listResult.add(convertToDTO(roleIdInt));
        }
        return listResult;
    } catch (Exception ex) {
        log.info(ex.getMessage());
        ex.printStackTrace();
        throw ex;
    }
}


  private DetailRolePrivilegeDTO convertToDTO(Integer roleIdInt) {
    CategoryCode roleId = categoryCodeRepository.findByCategoryCodeId((long) roleIdInt).orElseThrow(() -> new EntityNotFoundException("Role ID Not Found!"));
    List<RolePrivilege> listRole = rolePrivilegeRepository.findByIdList(roleId);
    List<ListDetailPrivilegeDTO> list = listRole.stream().map(this::listDetailPrivilegeDTO)
        .collect(Collectors.toList());
    return DetailRolePrivilegeDTO.builder()
        .roleId(roleId.getCategoryCodeId())
        .roleName(roleId.getCodeName())
        .listPrivilege(list)
        .build();
  }
  
  private ListDetailPrivilegeDTO listDetailPrivilegeDTO(RolePrivilege rolePrivilege) {
        return ListDetailPrivilegeDTO.builder()
                                     .rolePrivilegeId(rolePrivilege.getRolePrivilegeId())
                                     .privilegeId(rolePrivilege.getPrivilegeId().getCategoryCodeId())
                                     .privilegeName(rolePrivilege.getPrivilegeId().getCodeName())
                                     .build();
  }

  public DetailRolePrivilegeDTO getDetailRolePrivilege(Long id) {
    CategoryCode roleId = categoryCodeRepository.findByCategoryCodeId(id).orElseThrow(() -> new EntityNotFoundException("Role ID Not Found!"));
    List<RolePrivilege> listRolePrivilege = rolePrivilegeRepository.findByIdList(roleId);
    List<ListDetailPrivilegeDTO> detailRolePrivilegeDTO = listRolePrivilege.stream()
        .map(this::converListDetailPrivilegeDTO).collect(Collectors.toList());
    return DetailRolePrivilegeDTO.builder()
                                 .roleId(roleId.getCategoryCodeId())
                                 .roleName(roleId.getCodeName())
                                 .listPrivilege(detailRolePrivilegeDTO)
                                 .build();
  }

  private ListDetailPrivilegeDTO converListDetailPrivilegeDTO(RolePrivilege rolePrivilege) {
    return ListDetailPrivilegeDTO.builder()
   .rolePrivilegeId(rolePrivilege.getRolePrivilegeId())
   .privilegeId(rolePrivilege.getPrivilegeId().getCategoryCodeId())
   .privilegeName(rolePrivilege.getPrivilegeId().getCodeName())
   .build();
  }

  public RolePrivilege getRolePrivilegeId(Long rolePrivilegeId) {
      return rolePrivilegeRepository.findByRolePrivilegeId(rolePrivilegeId)
        .orElseThrow(() -> new EntityNotFoundException("Role Privilege ID Not Found!"));
  }

  CategoryCode getCategoryId(Long id){
    return categoryCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role Id Not Found!"));
  }

  @Transactional
  public RolePrivilege createRolePrivilege(AddRolePrivilegeDTO rolePrivilege){
    try {
      CategoryCode idRole = getCategoryId(rolePrivilege.getRoleId());
      RolePrivilege role = new RolePrivilege();
      for(Long id : rolePrivilege.getListPrivilege()){
        CategoryCode privilege = categoryCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Privilege Id Not Found!"));
        Optional<RolePrivilege> existingUserRole = rolePrivilegeRepository.findByUserRoleAndPrivilege(idRole, privilege );
        if(!existingUserRole.isPresent()) {
          role = RolePrivilege.builder()
                              .rolePrivilegeId(rolePrivilege.getRolePrivilegeId())
                              .roleId(idRole)
                              .privilegeId(privilege)
                              .isActive(true)
                              .createdBy(rolePrivilege.getCreatedBy())
                              .createdOn(new Timestamp(System.currentTimeMillis()))
                              .updateBy(rolePrivilege.getUpdatedBy())
                              .updateOn(new Timestamp(System.currentTimeMillis()))
                              .build();
          rolePrivilegeRepository.save(role);
        } else {
          throw new EntityExistsException("Role Privilege Data Already Exist!");
        } 
      } 
      return role; 
    } catch (Exception ex) {
        log.info(ex.getMessage());
        ex.printStackTrace();
        throw ex;
    }
  }

  public ResponseAddRolePrivilegeDTO convertToAddResponseRollPrivilege(RolePrivilege role){
    List<RolePrivilege> rolePrivilege = rolePrivilegeRepository.findByIdList(role.getRoleId());
    List<ListDetailPrivilegeDTO> listPrivilege = rolePrivilege.stream().map(this::convertRolePrivilegeDTO).collect(Collectors.toList());
    return ResponseAddRolePrivilegeDTO.builder()
                                      .rolePrivilegeId(role.getRolePrivilegeId())
                                      .roleId(role.getRoleId().getCategoryCodeId())
                                      .roleName(role.getRoleId().getCodeName())
                                      .listPrivilege(listPrivilege)
                                      .isActive(true)
                                      .createdBy(role.getCreatedBy())
                                      .createdOn(new Timestamp(System.currentTimeMillis()))
                                      .updatedBy(role.getUpdateBy())
                                      .updatedOn(new Timestamp(System.currentTimeMillis()))
                                      .build();
  }

  List<RolePrivilege> getRoleActive(Long id){
    CategoryCode categoryId = categoryCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role Not Found"));
    return rolePrivilegeRepository.findIdActive(categoryId);
  }

  public ResponseUpdateRolePrivilegeDTO responseUpdateRolePrivilegeDTO(Long id) {
    CategoryCode categoryId = categoryCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role Not Found"));
    List<RolePrivilege> userRoles = rolePrivilegeRepository.findIdActive(categoryId);
    List<ListDetailPrivilegeDTO> userRoleDTOs = userRoles.stream()
            .map(this::convertRolePrivilegeDTO)
            .collect(Collectors.toList());

      return ResponseUpdateRolePrivilegeDTO.builder()
            .roleId(id)
            .nameRole(categoryId.getCodeName())
            .privilegeId(userRoleDTOs)
            .build();
}

@Transactional
public List<RolePrivilege> updateRolePrivilege(Long id, UpdateRolePrivilegeDTO userRoleDTO) {
    try {
      CategoryCode categoryId = categoryCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id Not Found"));
        List<RolePrivilege> userRoles = rolePrivilegeRepository.findByIdList(categoryId);
        for (RolePrivilege uRole : userRoles) {
            uRole.setActive(false);
            uRole.setUpdateOn(new Timestamp(System.currentTimeMillis()));
        }
        rolePrivilegeRepository.saveAll(userRoles);
        updateRolePrivilege(id, userRoles, userRoleDTO);
        return userRoles;
    } catch (Exception ex) {
        log.info(ex.getMessage());
        ex.printStackTrace();
        throw ex;
    }
}

@Transactional
public void updateRolePrivilege(Long id, List<RolePrivilege> usersRole, UpdateRolePrivilegeDTO userRoleDTO) {
  try {
    for(int i = 0; i< userRoleDTO.getPrivilegeId().size(); i++) {
            CategoryCode categoryRoleId = categoryCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id Not Found"));
            Long privilegeId = userRoleDTO.getPrivilegeId().get(i);

            CategoryCode privilegeCode = categoryCodeRepository.findById(privilegeId)
                    .orElseThrow(() -> new EntityNotFoundException("Privilege ID Not Found!"));

            Optional<RolePrivilege> existingUserRole = rolePrivilegeRepository.findByUserRoleAndPrivilege(categoryRoleId, privilegeCode);

            if (existingUserRole.isPresent()) {
                existingUserRole.get().setActive(true);
                existingUserRole.get().setUpdateOn(new Timestamp(System.currentTimeMillis()));
                existingUserRole.get().setUpdateBy(userRoleDTO.getUpdatedBy());
                rolePrivilegeRepository.save(existingUserRole.get());
            } else {
                RolePrivilege newUserRole = RolePrivilege.builder()
                        .roleId(categoryRoleId)
                        .privilegeId(privilegeCode)
                        .isActive(true)
                        .createdOn(new Timestamp(System.currentTimeMillis()))
                        .build();
                rolePrivilegeRepository.save(newUserRole);
            }
        }
  } catch (Exception ex) {
    log.info(ex.getMessage());
    ex.printStackTrace();
    throw ex;
  }
}

  private ListDetailPrivilegeDTO convertRolePrivilegeDTO(RolePrivilege rolePrivilege) {
    return ListDetailPrivilegeDTO.builder()
            .rolePrivilegeId(rolePrivilege.getRoleId().getCategoryCodeId())
            .privilegeId(rolePrivilege.getPrivilegeId().getCategoryCodeId())
            .privilegeName(rolePrivilege.getPrivilegeId().getCodeName())
            .build();
  }

  @Transactional
  public RolePrivilege createRolePrivilegeMapper(RolePrivilegeDTO rolePrivilegeDto) {
    RolePrivilege rolePrivilege = RolePrivilege.builder()
        .roleId(
            categoryCodeRepository.findById(rolePrivilegeDto.getRoleId()).orElseThrow(() -> new EntityNotFoundException("role Id Not Found")))
        .privilegeId(categoryCodeRepository.findById(rolePrivilegeDto.getPrivilegeId())
            .orElseThrow(() -> new EntityNotFoundException("privilege Id Not Found")))
        .build();
    rolePrivilegeRepository.save(rolePrivilege);
    return rolePrivilege;
  }
}
