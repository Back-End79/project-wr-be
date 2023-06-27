package com.tujuhsembilan.wrcore.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.wrcore.dto.UserDTO;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import lombok.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
  private final UserRepository userRepository;
  private final CategoryCodeRepository categoryCodeRepository;

  public Page<Users> getAllUser(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  public Users createEmployee(UserDTO usersDto) throws NotFoundException {
    Optional<Users> isUsernameExist = userRepository.findByUserName(usersDto.getUserName());
    Optional<Users> isNipExist = userRepository.findByNip(usersDto.getNip());
    if (isUsernameExist.isPresent()) {
      throw new IllegalArgumentException("Username Already Registered, Try Another Username!");
    } else if (isNipExist.isPresent()) {
      throw new IllegalArgumentException("NIP Already Registered, Try Another NIP!");
    }
    Users users = createUsersMapper(usersDto);
    userRepository.save(users);
    return users;
  }

  public Users getUserById(Long userId) throws NotFoundException {
    Users user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException());
    return user;
  }

  public Users updateUser(Long userId, UserDTO userDto) throws NotFoundException {
    Users existingUser = getUserById(userId);
    CategoryCode jobType = getCategoryCodeById(userDto.getJobTypeId());
    CategoryCode placementType = getCategoryCodeById(userDto.getPlacementType());
    CategoryCode group = getCategoryCodeById(userDto.getGroup());
    CategoryCode position = getCategoryCodeById(userDto.getPosition());
    CategoryCode statusOnsite = getCategoryCodeById(userDto.getStatusOnsite());
    CategoryCode ptkpStatus = getCategoryCodeById(userDto.getPtkpStatus());

    Users updatedUser = UpdatedUserMapper(existingUser, userDto, jobType, placementType, group, position, statusOnsite,
        ptkpStatus);

    Users savedUser = userRepository.save(updatedUser);
    return savedUser;
  }

  private boolean isEndThisMonth(Date timestamp) {
    LocalDate currentDate = LocalDate.now();
    LocalDate timestampDate = LocalDate.now();
    YearMonth currentYearMonth = YearMonth.from(currentDate);
    YearMonth timestampYearMonth = YearMonth.from(timestampDate);
    return timestampYearMonth.equals(currentYearMonth);
  }

  public List<Users> getUsersByName(String userName) {
    return userRepository.findByUserNameContainingIgnoreCase(userName);
  }

  private CategoryCode getCategoryCodeById(Long categoryId) throws NotFoundException {
    return categoryCodeRepository.findByCategoryCodeId(categoryId)
        .orElseGet(() -> new CategoryCode());
  }

  public Users createUsersMapper(UserDTO usersDto) throws NotFoundException {
    return Users.builder()
        .statusOnsite(
            categoryCodeRepository.findById(usersDto.getStatusOnsite()).orElseThrow(() -> new NotFoundException()))
        .group(categoryCodeRepository.findById(usersDto.getGroup()).orElseThrow(() -> new NotFoundException()))
        .position(categoryCodeRepository.findById(usersDto.getPosition()).orElseThrow(() -> new NotFoundException()))
        .placementType(
            categoryCodeRepository.findById(usersDto.getPlacementType()).orElseThrow(() -> new NotFoundException()))
        .ptkpStatus(
            categoryCodeRepository.findById(usersDto.getPtkpStatus()).orElseThrow(() -> new NotFoundException()))
        .jobTypeId(categoryCodeRepository.findById(usersDto.getJobTypeId()).orElseThrow(() -> new NotFoundException()))
        .createdBy(usersDto.getCreatedBy())
        .bpjsClass(usersDto.getBpjsClass())
        .nip(usersDto.getNip())
        .ssoId(usersDto.getSsoId())
        .npwp(usersDto.getNpwp())
        .email(usersDto.getEmail())
        .photoProfile(usersDto.getPhotoProfile())
        .school(usersDto.getSchool())
        .firstName(usersDto.getFirstName())
        .lastName(usersDto.getLastName())
        // .phoneNumber(usersDto.getPhoneNumber())
        .userName(usersDto.getUserName())
        .education(usersDto.getEducation())
        .department(usersDto.getDepartment())
        .bpjsKesehatan(usersDto.getBpjsKesehatan())
        // .major(usersDto.getMajor())
        .postalCode(usersDto.getPostalCode())
        // .division(usersDto.getDivision())
        // .generation(usersDto.getGeneration())
        .placeOfBirth(usersDto.getPlaceOfBirth())
        .identityNumber(usersDto.getIdentityNumber())
        .familyRelationship(usersDto.getFamilyRelationship())
        // .familyRelationshipName(usersDto.getFamilyRelationshipName())
        .familyRelationshipNumber(usersDto.getFamilyRelationshipNumber())
        .lastContractStatus(usersDto.getLastContractStatus())
        .numberOfDependents(usersDto.getNumberOfDependents())
        .carrerStartDate(usersDto.getCarrerStartDate())
        .department(usersDto.getDepartment())
        .no(usersDto.getNo())
        // .contractEndDate(usersDto.getContractEndDate())
        .lastContractDate(usersDto.getLastContractDate())
        .joinDate(usersDto.getJoinDate())
        .lastModifiedBy(usersDto.getLastModifiedBy())
        .dateOfBirth(usersDto.getDateOfBirth())
        .createdOn(new Timestamp(System.currentTimeMillis()))
        .isActive(false)
        .lastModifiedOn(usersDto.getLastModifiedOn())
        .endContractThisMonth(isEndThisMonth(usersDto.getLastContractDate()))
        .build();
  }

  private Users UpdatedUserMapper(Users existingUser, UserDTO userDto, CategoryCode jobType,
      CategoryCode placementType, CategoryCode group, CategoryCode position,
      CategoryCode statusOnsite, CategoryCode ptkpStatus) {
    return Users.builder()
        .userId(existingUser.getUserId())
        .jobTypeId(jobType)
        .nip(userDto.getNip())
        .placementType(placementType)
        .ssoId(userDto.getSsoId())
        .group(group)
        .position(position)
        .statusOnsite(statusOnsite)
        .userName(userDto.getUserName())
        .email(userDto.getEmail())
        .lastContractStatus(userDto.getLastContractStatus())
        .lastContractDate(existingUser.getLastContractDate())
        .firstName(userDto.getFirstName())
        .lastName(userDto.getLastName())
        .joinDate(existingUser.getJoinDate())
        .endContractThisMonth(existingUser.isEndContractThisMonth())
        .isActive(existingUser.isActive())
        .photoProfile(userDto.getPhotoProfile())
        .lastModifiedOn(new Timestamp(System.currentTimeMillis()))
        .lastModifiedBy(userDto.getLastModifiedBy())
        .createdBy(userDto.getCreatedBy())
        .createdOn(existingUser.getCreatedOn())
        .placeOfBirth(userDto.getPlaceOfBirth())
        .dateOfBirth(existingUser.getDateOfBirth())
        .identityNumber(userDto.getIdentityNumber())
        .postalCode(userDto.getPostalCode())
        .familyRelationship(userDto.getFamilyRelationship())
        .familyRelationshipNumber(userDto.getFamilyRelationshipNumber())
        .school(userDto.getSchool())
        .education(userDto.getEducation())
        .bpjsKesehatan(userDto.getBpjsKesehatan())
        .numberOfDependents(userDto.getNumberOfDependents())
        .bpjsClass(userDto.getBpjsClass())
        .carrerStartDate(existingUser.getCarrerStartDate())
        .ptkpStatus(ptkpStatus)
        .npwp(userDto.getNpwp())
        .no(userDto.getNo())
        .department(userDto.getDepartment())
        .build();
  }

}
