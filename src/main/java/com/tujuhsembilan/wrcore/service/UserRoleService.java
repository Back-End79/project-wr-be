package com.tujuhsembilan.wrcore.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.UserRoleDTO;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.UserRole;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import com.tujuhsembilan.wrcore.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserRoleService {
  private final UserRoleRepository userRoleRepository;
  private final UserRepository userRepository;
  private final CategoryCodeRepository categoryCodeRepository;

  public List<UserRole> getAllUserRole() {
    List<UserRole> users = userRoleRepository.findAll();
    return users;
  }

  public UserRole addUserRole(UserRoleDTO userRoleDto) throws NotFoundException {
    Optional<Users> isUserExist = userRepository.findById(userRoleDto.getUserId());
    Optional<UserRole> isRoleExist = userRoleRepository.findById(userRoleDto.getRoleId());
    // if(isUserExist.isPresent()&& isRoleExist.isPresent()){
    UserRole userRole = UserRole.builder()
        .userId(userRoleDto.getUserId())
        .roleId(categoryCodeRepository.findById(userRoleDto.getRoleId()).orElseThrow(() -> new NotFoundException()))
        .build();
    userRoleRepository.save(userRole);
    return userRole;
    // } else if(!isRoleExist.isPresent()){
    // throw new IllegalArgumentException("ID User Role Not Found! ");
    // }
    // else {
    // throw new IllegalArgumentException("User Not Found! ");
    // }

  }

  public void deleteRoleUser(Long id) {
    Optional<UserRole> isExist = userRoleRepository.findById(id);
    if (isExist.isPresent()) {
      userRoleRepository.deleteById(id);
    } else {
      throw new IllegalArgumentException("Data Not Found");
    }
  }

  public UserRole updateUserRole(Long id, UserRoleDTO userRoleDto) throws NotFoundException {
    Optional<UserRole> usersRole = userRoleRepository.findById(id);
    Optional<CategoryCode> isIdExist = categoryCodeRepository.findById(userRoleDto.getRoleId());
    // if(usersRole.isPresent()&&isIdExist.isPresent()){
    usersRole.get()
        .setRoleId(categoryCodeRepository.findById(userRoleDto.getRoleId()).orElseThrow(() -> new NotFoundException()));
    userRoleRepository.save(usersRole.get());
    return usersRole.get();
    // } else if(!isIdExist.isPresent()) {
    // throw new IllegalArgumentException("Data User Role Not Found");
    // } else {
    // throw new IllegalArgumentException("Data Not Found");
    // }
  }
}
