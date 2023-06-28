package com.tujuhsembilan.wrcore.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.model.Users;
import com.tujuhsembilan.wrcore.dto.EmployeeDTO;
import com.tujuhsembilan.wrcore.dto.UserDTO;
import com.tujuhsembilan.wrcore.service.UserService;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
  private final UserService userService;
  private final MessageUtil msg;


  @GetMapping
  public ResponseEntity<?> getAllEmployee(
      @RequestParam(value = "employeeName", required = false) String employeeName,
      Pageable pageable
  ) {
      int pageSize = 10;
      int pageNumber = pageable.getPageNumber();
      Pageable updatedPageable = PageRequest.of(pageNumber, pageSize, pageable.getSort());

      if (employeeName != null && !employeeName.isEmpty()) {
          List<Users> employees = userService.getUsersByName(employeeName);
          List<EmployeeDTO> employeeDto = employees.stream()
              .map(this::buildEmployeDTO)
              .collect(Collectors.toList());
          return ResponseEntity.ok(CollectionModel.of(employeeDto));
      } else {
          Page<Users> employeePage = userService.getAllUser(updatedPageable);
          Page<EmployeeDTO> employeeDto = employeePage.map(this::buildEmployeDTO);
          // return ResponseEntity.ok(CollectionModel.of(employeeDto));
          return ResponseEntity.ok(employeeDto);
      }
  }

  @PostMapping("/addEmployee")
  public ResponseEntity<?> addEmployee(@RequestBody UserDTO usersDto) throws NotFoundException {
    try {
      var o = userService.createEmployee(usersDto);
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .model(EntityModel.of(o))
              .meta("message", msg
                  .get("application.success.created", "Employee"))
              .build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(JsonApiModelBuilder
              .jsonApiModel()
              .meta("message", e.getMessage())
              .build());
    }
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getUserById(@PathVariable Long userId) throws NotFoundException {
    Users user = userService.getUserById(userId);

    if (user != null) {
      UserDTO userDTO = buildUserDTO(user);

      return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(userDTO));
    }

    return ResponseEntity.notFound().build();
  }

  @PutMapping("/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDto)
      throws NotFoundException {
    Users updatedUser = userService.updateUser(userId, userDto);

    if (updatedUser != null) {
      UserDTO updatedUserDTO = buildUserDTO(updatedUser);

      return ResponseEntity.status(HttpStatus.OK)
          .body(JsonApiModelBuilder.jsonApiModel().model(EntityModel.of(updatedUserDTO))
              .meta("message", msg.get("application.success.updated", "User")).build());
    }

    return ResponseEntity.notFound().build();
  }

  private UserDTO buildUserDTO(Users user) {
    return UserDTO.builder()
        .userId(user.getUserId())
        .jobTypeId(user.getJobTypeId().getCategoryCodeId())
        .nip(user.getNip())
        .placementType(user.getPlacementType().getCategoryCodeId())
        .ssoId(user.getSsoId())
        .group(user.getGroup().getCategoryCodeId())
        .position(user.getPosition().getCategoryCodeId())
        .statusOnsite(user.getStatusOnsite().getCategoryCodeId())
        .userName(user.getUserName())
        .email(user.getEmail())
        .lastContractStatus(user.getLastContractStatus())
        .lastContractDate(user.getLastContractDate())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .joinDate(user.getJoinDate())
        .endContractThisMonth(user.isEndContractThisMonth())
        .isActive(user.isActive())
        .photoProfile(user.getPhotoProfile())
        .lastModifiedOn(user.getLastModifiedOn())
        .lastModifiedBy(user.getLastModifiedBy())
        .createdBy(user.getCreatedBy())
        .createdOn(user.getCreatedOn())
        .placeOfBirth(user.getPlaceOfBirth())
        .dateOfBirth(user.getDateOfBirth())
        .identityNumber(user.getIdentityNumber())
        .postalCode(user.getPostalCode())
        .familyRelationship(user.getFamilyRelationship())
        .familyRelationshipNumber(user.getFamilyRelationshipNumber())
        .school(user.getSchool())
        .education(user.getEducation())
        .bpjsKesehatan(user.getBpjsKesehatan())
        .numberOfDependents(user.getNumberOfDependents())
        .bpjsClass(user.getBpjsClass())
        .carrerStartDate(user.getCarrerStartDate())
        .ptkpStatus(user.getPtkpStatus().getCategoryCodeId())
        .npwp(user.getNpwp())
        .no(user.getNo())
        .department(user.getDepartment())
        .build();
  }

  private EmployeeDTO buildEmployeDTO(Users users) {
    return EmployeeDTO.builder()
          .nip(users.getNip())
          .fullName(users.getFirstName() + " " + users.getLastName())
          .photoProfile(users.getPhotoProfile())
          .lastContractStatus(users.getLastContractStatus())
          .lastContractDate(users.getLastContractDate())
          .build();
  }
}
