package com.tujuhsembilan.wrcore.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.ResponseAddUserRoleDTO;
import com.tujuhsembilan.wrcore.dto.AddUserRoleDTO;
import com.tujuhsembilan.wrcore.dto.DetailRoleDTO;
import com.tujuhsembilan.wrcore.dto.DetailUserRoleDTO;
import com.tujuhsembilan.wrcore.dto.ResponseUpdateUserRoleDTO;
import com.tujuhsembilan.wrcore.dto.UpdateUserRoleDTO;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.UserRole;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import com.tujuhsembilan.wrcore.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRoleService {
  private final UserRoleRepository userRoleRepository;
  private final UserRepository userRepository;
  private final CategoryCodeRepository categoryCodeRepository;

  public List<Map<String, Object>> findAllUserRoleId(String search, Pageable pageable) {
    return userRoleRepository.findAllUserRoleId(pageable, search);
 }


  public List<DetailUserRoleDTO> convertToRolePrivilegeDTO(Pageable pageable, String search) {
      List<Map<String, Object>> listRoleID = userRoleRepository.findAllUserRoleId(pageable, search);
    try {
        List<DetailUserRoleDTO> listResult = new ArrayList<>();
        for (Map<String, Object> dri: listRoleID) {
            String test = Objects.toString(dri.get("user_id"));
            Integer roleIdInt =  Integer.parseInt(test);
            listResult.add(convertToDetailUserRoleDTO(roleIdInt));
        }
        return listResult;
    } catch (Exception ex) {
        log.info(ex.getMessage());
        ex.printStackTrace();
        throw ex;
    }
  }

  private DetailUserRoleDTO convertToDetailUserRoleDTO(Integer userId) {
      Users users = userRepository.findByUserId((long)userId).orElseThrow(()->new EntityNotFoundException("Users Not Found!"));
      List<UserRole> listUserRoles = userRoleRepository.findByUserIdList(users);
      List<DetailRoleDTO> usersDTOs = listUserRoles.stream().map(this::convertUserRoleDTO).collect(Collectors.toList());
  return DetailUserRoleDTO.builder()
                          .userRoleId(users.getUserId())
                          .userId(users.getUserId())
                          .firstName(users.getFirstName())
                          .lastName(users.getLastName())
                          .nip(users.getNip())
                          .listRole(usersDTOs)
                          .build();
  }

  Users getUserActive(Long id){
    return userRepository.findByIdAndActive(id).orElseThrow(() -> new EntityNotFoundException("User Data Not Found!"));
  }

  @Transactional
  public UserRole addUserRole(AddUserRoleDTO userRoleDto) {
    try {
      Users isUserExist = getUserActive(userRoleDto.getUserId());
      UserRole userRole = new UserRole();
      for(Long roleId : userRoleDto.getRoleId()){
        CategoryCode isRoleExist = categoryCodeRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Role Id Not Found!"));
        Optional<UserRole> isUserRoleExist = userRoleRepository.findByUserRoleIdAndUserId(isRoleExist, isUserExist);
        if(!isUserRoleExist.isPresent()){
           userRole = UserRole.builder()
                           .userId(isUserExist)
                           .roleId(isRoleExist)
                           .isActive(true)
                           .lastModifiedOn(new Timestamp(System.currentTimeMillis()))
                           .lastModifiedBy(userRoleDto.getLastModifiedBy())
                           .createdBy(userRoleDto.getCreatedBy())
                           .createdOn(new Timestamp(System.currentTimeMillis()))
                           .build();
           userRoleRepository.save(userRole);
        } else {
          throw new EntityExistsException("User Role Data Already Exist!");
        }        
      }
      return userRole;
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }

  public ResponseAddUserRoleDTO convertAddUserRoleDTO(UserRole userRole) {
    List<UserRole> userRoles = userRoleRepository.findByIdAndActiveList(userRole.getUserId());
    List<DetailRoleDTO> userRoleDTOs = userRoles.stream().map(this::convertUserRoleDTO).collect(Collectors.toList());
      return ResponseAddUserRoleDTO.builder()
                                                 .userRoleId(userRole.getUserRoleId())
                                                 .detailRole(userRoleDTOs)
                                                 .userId(userRole.getUserId().getUserId())
                                                 .isActive(userRole.isActive())
                                                 .lastModifiedOn(userRole.getLastModifiedOn())
                                                 .lastModifiedBy(userRole.getLastModifiedBy())
                                                 .createdBy(userRole.getCreatedBy())
                                                 .createdOn(userRole.getCreatedOn())
                                                 .build();
  }

  public ResponseUpdateUserRoleDTO convertUpdateUserRoleDTO(Long id) {
    Users users = getUserActive(id);
    List<UserRole> userRoles = userRoleRepository.findByIdAndActiveList(users);
    List<DetailRoleDTO> userRoleDTOs = userRoles.stream().map(this::convertUserRoleDTO).collect(Collectors.toList());    
    ResponseUpdateUserRoleDTO updateUserRoleDTO = ResponseUpdateUserRoleDTO.builder()
                                                           .userId(id)
                                                           .detailRole(userRoleDTOs)
                                                           .build();   
    return updateUserRoleDTO;
  }

  @Transactional
  public List<UserRole> updateUserRole(Long id, UpdateUserRoleDTO userRoleDTO) {
    try {
      Users users = getUserActive(id);
      List<UserRole> userRole = userRoleRepository.findByIdAndActiveList(users);
      for(UserRole uRole : userRole){
        uRole.setActive(false);
      }
      userRoleRepository.saveAll(userRole);
      checkUserRole(id, userRole, userRoleDTO);
      return userRole;
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }

  @Transactional
  public void checkUserRole (Long id, List<UserRole> userRoles, UpdateUserRoleDTO userRoleDTO) {
    try {
      for(int i = 0; i< userRoleDTO.getRoleId().size(); i++){      
      Users users = getUserActive(id);
      CategoryCode categoryCode = categoryCodeRepository.findById(userRoleDTO.getRoleId().get(i)).orElseThrow(() -> new EntityNotFoundException("Role Id Not Found!"));
      Optional<UserRole> uOptional = userRoleRepository.findByUserRoleIdAndUserId(categoryCode, users);
      if(uOptional.isPresent()){
        uOptional.get().setActive(true);
        uOptional.get().setLastModifiedOn(new Timestamp(System.currentTimeMillis()));
        uOptional.get().setLastModifiedBy(userRoleDTO.getLastModifiedBy());
        userRoleRepository.save(uOptional.get());
      } else {
         CategoryCode roleId = categoryCodeRepository.findById(userRoleDTO.getRoleId().get(i)).orElseThrow(() -> new EntityNotFoundException("Role Id Not Found!"));
         UserRole userRole = UserRole.builder()
                                     .userId(users)
                                     .roleId(roleId)
                                     .isActive(true)
                                     .lastModifiedOn(new Timestamp(System.currentTimeMillis()))
                                     .lastModifiedBy(userRoleDTO.getLastModifiedBy())
                                     .createdBy(userRoleDTO.getLastModifiedBy())
                                     .createdOn(new Timestamp(System.currentTimeMillis()))
                                     .build();
         userRoleRepository.save(userRole);
      }     
    }
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }

  @Transactional
  public void deleteRoleUser(Long id) {
    try {      
      Users users = getUserActive(id);
      List<UserRole> userRoles = userRoleRepository.findByIdAndActiveList(users);
      for(UserRole uRole : userRoles){
        uRole.setActive(false);
        uRole.setLastModifiedOn(new Timestamp(System.currentTimeMillis()));
      }
      userRoleRepository.saveAll(userRoles);      
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }    
  }

  public DetailUserRoleDTO getDetailUserRole(Long id)  {
    Users users = getUserActive(id);
    List<UserRole> userRole = userRoleRepository.findByIdAndActiveList(users);
    List<DetailRoleDTO> userRoleDTOs = userRole.stream().map(this::convertUserRoleDTO).collect(Collectors.toList());
    return DetailUserRoleDTO.builder()
                            .userRoleId(id)
                            .userId(users.getUserId())
                            .firstName(users.getFirstName())
                            .lastName(users.getLastName())
                            .nip(users.getNip())
                            .listRole(userRoleDTOs)
                            .build();
  }

  private DetailRoleDTO convertUserRoleDTO(UserRole userRole){
    return DetailRoleDTO.builder()
                      .userRoleId(userRole.getUserRoleId())
                      .roleId(userRole.getRoleId().getCategoryCodeId())
                      .role(userRole.getRoleId().getCodeName())
                      .isActive(userRole.isActive())                      
                      .build();
  }
}
