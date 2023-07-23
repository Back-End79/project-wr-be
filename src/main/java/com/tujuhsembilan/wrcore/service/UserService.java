package com.tujuhsembilan.wrcore.service;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.ol.dto.TeamMemberDTO;
import com.tujuhsembilan.wrcore.ol.dto.UserOLDTO;
import com.tujuhsembilan.wrcore.dto.EmployeeDTO;
import com.tujuhsembilan.wrcore.dto.UserDTO;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import com.tujuhsembilan.wrcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityNotFoundException;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
  private final UserRepository userRepository;
  private final CategoryCodeRepository categoryCodeRepository;

  public Page<Users> getAllEmployee(Pageable pageable, String search){
    return userRepository.findUser(search, pageable);
  }

  public List<EmployeeDTO> convertToListEmployee(Page<Users> user){
    List<EmployeeDTO> employeeDTO = user.getContent().stream()
                                               .map(this::convertToDto)
                                               .collect(Collectors.toList());
    return employeeDTO;
  }

  private EmployeeDTO convertToDto(Users users) {
    return EmployeeDTO.builder()
                      .userId(users.getUserId())
                      .nip(users.getNip())
                      .fullName(users.getFirstName() + " " + users.getLastName())
                      .photoProfile(users.getPhotoProfile())
                      .lastContractStatus(users.getLastContractStatus())
                      .assingment(users.getStatusOnsite().getCodeName())
                      .lastContractDate(users.getLastContractDate())
                      .build();
  }

  public Users getUserById(Long userId) {
    Users user = userRepository.findByUserId(userId).orElseThrow(() -> new EntityNotFoundException("User ID Not Found"));
    return user;
  }

  public Users updateUser(Long userId, UserDTO userDto) {
    Users existingUser = getUserById(userId);
    CategoryCode jobType = getCategoryCodeById(userDto.getJobTypeId());
    CategoryCode placementType = getCategoryCodeById(userDto.getPlacementType());
    CategoryCode group = getCategoryCodeById(userDto.getGroup());
    CategoryCode position = getCategoryCodeById(userDto.getPosition());
    CategoryCode statusOnsite = getCategoryCodeById(userDto.getStatusOnsite());
    CategoryCode ptkpStatus = getCategoryCodeById(userDto.getPtkpStatus());

    Users updatedUser = buildUpdatedUser(existingUser, userDto, jobType, placementType, group, position, statusOnsite,
        ptkpStatus);

    Users savedUser = userRepository.save(updatedUser);
    return savedUser;
  }

  private CategoryCode getCategoryCodeById(Long categoryId) {
    return categoryCodeRepository.findByCategoryCodeId(categoryId)
        .orElseThrow(() ->  new EntityNotFoundException("Category Code Not Found!"));
  }

  private Users buildUpdatedUser(Users existingUser, UserDTO userDto, CategoryCode jobType,
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

  public Users createEmployee(UserDTO usersDto) {
      Users users = createUsersMapper(usersDto);
      userRepository.save(users);
      return users;           
    }

    public Users createUsersMapper(UserDTO usersDto) {
      String nip = generateNip(categoryCodeRepository.findById(usersDto.getGroup()).orElseThrow(() ->  new EntityNotFoundException("NIP Not Found!")));
        return Users.builder()
            .statusOnsite(categoryCodeRepository.findById(usersDto.getStatusOnsite()).orElseThrow(() ->  new EntityNotFoundException("Status OnSite Not Found!")))
            .group(categoryCodeRepository.findById(usersDto.getGroup()).orElseThrow(() ->  new EntityNotFoundException("Group Not Found!")))
            .position(categoryCodeRepository.findById(usersDto.getPosition()).orElseThrow(() ->  new EntityNotFoundException("Position Not Found!")))
            .placementType(categoryCodeRepository.findById(usersDto.getPlacementType()).orElseThrow(() ->  new EntityNotFoundException("Placement Type Not Found!")))
            .ptkpStatus(categoryCodeRepository.findById(usersDto.getPtkpStatus()).orElseThrow(() ->  new EntityNotFoundException("PTKP Status Not Found!")))
            .jobTypeId(categoryCodeRepository.findById(usersDto.getJobTypeId()).orElseThrow(() ->  new EntityNotFoundException("Job Type Not Found!")))
            .createdBy(usersDto.getCreatedBy())
            .bpjsClass(usersDto.getBpjsClass())            
            .nip(nip)
            .ssoId(usersDto.getSsoId())
            .npwp(usersDto.getNpwp())
            .email(usersDto.getEmail())
            .photoProfile(usersDto.getPhotoProfile())
            .school(usersDto.getSchool())            
            .firstName(usersDto.getFirstName())
            .lastName(usersDto.getLastName())
            .userName(nip + "-" + usersDto.getFirstName() + " " + usersDto.getLastName())
            .education(usersDto.getEducation())
            .department(usersDto.getDepartment())
            .bpjsKesehatan(usersDto.getBpjsKesehatan())
            .postalCode(usersDto.getPostalCode())
            .placeOfBirth(usersDto.getPlaceOfBirth())
            .identityNumber(usersDto.getIdentityNumber())
            .familyRelationship(usersDto.getFamilyRelationship())
            .familyRelationshipNumber(usersDto.getFamilyRelationshipNumber())
            .lastContractStatus(usersDto.getLastContractStatus())
            .numberOfDependents(usersDto.getNumberOfDependents())
            .carrerStartDate(usersDto.getCarrerStartDate())
            .lastContractDate(usersDto.getLastContractDate())
            .joinDate(usersDto.getJoinDate())
            .lastModifiedBy(usersDto.getLastModifiedBy())
            .dateOfBirth(usersDto.getDateOfBirth())            
            .createdOn(new Timestamp(System.currentTimeMillis()))
            .isActive(true)
            .lastModifiedOn(new Timestamp(System.currentTimeMillis()))
            .endContractThisMonth(isEndThisMonth(usersDto.getLastContractDate()))
            .no(usersDto.getNo())
            .build();
    }

    public List<UserOLDTO> getUserOL(String search){
      List<UserOLDTO> uList = userRepository.getOlUser(search.toLowerCase());
      return uList;
    }

    public Page<TeamMemberDTO> getTeamMember(Pageable pageable, String search){
      Page<TeamMemberDTO> teamMember = userRepository.findByIsActiveAndTalent(search.toLowerCase(), pageable);
      return teamMember; 
    }

    private boolean isEndThisMonth(Date contractEndDate){  
        LocalDate localDate = contractEndDate.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.from(currentDate);
        YearMonth timestampYearMonth = YearMonth.from(localDate);
        return timestampYearMonth.equals(currentYearMonth);
    }

    private String generateNip(CategoryCode categoryCode){
      LocalDate currentDate = LocalDate.now();
      int countData = userRepository.countUser() + 1;   
      int year = Year.from(currentDate).getValue() % 100;
      Month month = Month.from(currentDate);
      int codeGroup = (categoryCode.getCategoryCodeId() == 8) ? 2 : 1;      
      return String.format("%d/%d/%d/%d", codeGroup, month.getValue(), year, countData);
    }

}
