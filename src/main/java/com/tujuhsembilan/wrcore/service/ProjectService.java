package com.tujuhsembilan.wrcore.service;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.toedter.spring.hateoas.jsonapi.JsonApiError;
import com.tujuhsembilan.wrcore.dto.*;
import com.tujuhsembilan.wrcore.model.*;
import com.tujuhsembilan.wrcore.ol.dto.ProjectOLDTO;
import com.tujuhsembilan.wrcore.repository.*;
import com.tujuhsembilan.wrcore.util.constant.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final CompanyRepository companyRepository;
  private final CategoryCodeRepository categoryCodeRepository;
  private final UserRepository userRepository;
  private final UserUtilizationRepository userUtilizationRepository;
  private final UserRoleRepository userRoleRepository;

  public Page<Project> getAllProject(Pageable pageable) {
    return projectRepository.findAll(pageable);
  }

  @Transactional
  public Project addProject(AddProjectDTO addProjectDTO) {
    try {
      List<Boolean> permissionList = new ArrayList<>();
      Users users = getUser(addProjectDTO.getCreatedBy(), ConstantMessage.USER_NOT_FOUND);
      List<UserRole> userRole = userRoleRepository.findByIdAndActiveList(users);
      for(UserRole uRole : userRole){        
        Boolean isHavePermission = projectRepository.hasValidRoleForAddProject(uRole.getRoleId().getCategoryCodeId());
        if(isHavePermission.equals(true)){
          permissionList.add(true);
        }
      }
      if(permissionList.contains(true)){
        Company company = getCompany(addProjectDTO.getCompanyId(), ConstantMessage.COMPANY_NOT_FOUND);
        CategoryCode projectType = getCategoryCode(addProjectDTO.getProjectType(), ConstantMessage.PROJECT_TYPE_NOT_FOUND);
        Project project = Project.builder()
                                 .companyId(company)
                                 .picProjectName(addProjectDTO.getPicProjectName())
                                 .picProjectPhone(addProjectDTO.getPicProjectPhone())
                                 .description(addProjectDTO.getDescription())
                                 .startDate(addProjectDTO.getStartDate())
                                 .endDate(addProjectDTO.getEndDate())
                                 .projectType(projectType)
                                 .isActive(true)
                                 .createdOn(new Timestamp(System.currentTimeMillis()))
                                 .lastModifiedOn(new Timestamp(System.currentTimeMillis()))
                                 .createdBy(addProjectDTO.getCreatedBy())
                                 .initialProject(addProjectDTO.getInitialProject())
                                 .projectName(addProjectDTO.getProjectName())
                                 .build();
        projectRepository.save(project);
        for(AddProjectTeamMemberDTO addProjectTeamMemberDTO : addProjectDTO.getTeamMember()){
            Users teamMember = getUser(addProjectTeamMemberDTO.getUserId(), ConstantMessage.USER_NOT_FOUND);
            saveUserUtilization(teamMember, project, addProjectTeamMemberDTO);
        }
        return project;
      } else {        
        throw new ValidationException("You Don't Have Permission To Create Project!");
      }
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }  

  @Transactional
  private void saveUserUtilization(Users user, Project project, AddProjectTeamMemberDTO addProjectTeamMemberDTO) {
    try {
      CategoryCode roleProjectId = categoryCodeRepository.findRoleProjectId(CategoryCodeConstant.CATEGORY_ROLE_PROJECT, addProjectTeamMemberDTO
                                                         .getRoleProjectId())
                                                         .orElseThrow(() -> new EntityNotFoundException(ConstantMessage.ROLE_PROJECT_NOT_FOUND));
      UserUtilization userUtilization = UserUtilization.builder()
                                                       .userId(user)
                                                       .companyId(project.getCompanyId())
                                                       .projectId(project)
                                                       .roleProjectId(roleProjectId)
                                                       .startDate(addProjectTeamMemberDTO.getJoinDate())
                                                       .endDate(addProjectTeamMemberDTO.getEndDate())
                                                       .isActive(true)
                                                       .build();
          userUtilizationRepository.save(userUtilization);        
    } catch (Exception ex) {      
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }         
  }

  @Transactional
  public Project updateProject(Long id, AddProjectDTO projectDTO) {
    try {      
      List<Boolean> permissionList = new ArrayList<>();
      Users users = getUser(projectDTO.getLastModifiedBy(), ConstantMessage.USER_NOT_FOUND);
      Project project = getProject(id, ConstantMessage.PROJECT_NOT_FOUND);
      List<UserRole> userRole = userRoleRepository.findByIdAndActiveList(users);
      List<UserUtilization> uList = userUtilizationRepository.findByProjectIdAndUserIdList(project, users);
      
      for(UserRole uRole : userRole){        
        Boolean isHavePermission = projectRepository.hasValidRoleForUpdateProject(uRole.getRoleId().getCategoryCodeId());
        if(isHavePermission.equals(true)){
          permissionList.add(true);
        }
      }

      for(UserUtilization userUtilization : uList){
        Boolean isHavePermission = projectRepository.hasValidRoleForUpdateProject(userUtilization.getRoleProjectId().getCategoryCodeId());
        log.info(isHavePermission.toString());
        if(isHavePermission.equals(true)){
          permissionList.add(true);
        }
      }

      if(permissionList.contains(true)){
        Company company = getCompany(projectDTO.getCompanyId(), ConstantMessage.COMPANY_NOT_FOUND);
        CategoryCode projectType = getCategoryCode(projectDTO.getProjectType(), ConstantMessage.PROJECT_TYPE_NOT_FOUND);
        List<UserUtilization> userUtilizations = userUtilizationRepository.findByProjectIdList(project);
        for(int i = 0; i < userUtilizations.size(); i++){
          boolean isActive = userUtilizations.get(i).getUserId().getUserId().equals(projectDTO.getLastModifiedBy());
          userUtilizations.get(i).setActive(isActive);          
        }
        userUtilizationRepository.saveAll(userUtilizations);
        project.setProjectId(id);
        project.setCompanyId(company);
        project.setPicProjectName(projectDTO.getPicProjectName());
        project.setPicProjectPhone(projectDTO.getPicProjectPhone());
        project.setDescription(projectDTO.getDescription());
        project.setProjectName(projectDTO.getProjectName());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setProjectType(projectType);
        project.setInitialProject(projectDTO.getInitialProject());
        project.setLastModifiedBy(projectDTO.getLastModifiedBy());
        project.setLastModifiedOn(new Timestamp(System.currentTimeMillis()));
        projectRepository.save(project);
        checkTeamMember(projectDTO.getTeamMember(), project, userUtilizations);
        return project;
      } else if(permissionList.isEmpty()){
        throw new ValidationException(ConstantMessage.USER_NOT_FOUND);
      } else {
        throw new ValidationException("You Don't Have Permission To Update Project!");
      }
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
  }  

  @Transactional
  public void checkTeamMember(List<AddProjectTeamMemberDTO> addProjectTeamMemberDTO, Project project, List<UserUtilization> userUtilizations) {   
    for(AddProjectTeamMemberDTO aMemberDTO : addProjectTeamMemberDTO){
      try {        
        Users users = getUser(aMemberDTO.getUserId(), ConstantMessage.USER_NOT_FOUND);
        CategoryCode getRoleProject = getCategoryCode(aMemberDTO.getRoleProjectId(), ConstantMessage.ROLE_PROJECT_NOT_FOUND);
        Optional<UserUtilization> userUtilization = userUtilizationRepository.findByProjectIdAndUserId(project, users);
        if(userUtilization.isPresent()){
          userUtilization.get().setCompanyId(project.getCompanyId());
          userUtilization.get().setStartDate(aMemberDTO.getJoinDate());
          userUtilization.get().setEndDate(aMemberDTO.getEndDate());
          userUtilization.get().setRoleProjectId(getRoleProject);
          userUtilization.get().setActive(true);
          userUtilizationRepository.save(userUtilization.get());
        } else {
          saveUserUtilization(users, project, aMemberDTO);
        }
      } catch (Exception ex) {        
        log.info(ex.getMessage());
        ex.printStackTrace();
        throw ex;
      }
    }       
  }  

  @Transactional
  public void deleteProject(Long id, ProjectDTO projectDTO) {
    try {
      Project project = getProject(id, ConstantMessage.PROJECT_NOT_FOUND);
      List<UserUtilization> userUtilizations = userUtilizationRepository.findByProjectIdList(project);      
        project.setActive(false);
        project.setLastModifiedBy(projectDTO.getLastModifiedBy());
        project.setLastModifiedOn(new Timestamp(System.currentTimeMillis()));
        for(UserUtilization userUtilization : userUtilizations){
          userUtilization.setActive(false);
          userUtilizationRepository.save(userUtilization);
        }
        projectRepository.save(project);
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }  
  }  

  public List<ProjectOLDTO> findAllByProjectNameAndSearch(String search) {
    return projectRepository.findAllByProjectNameAndSearch(search.toLowerCase());
  }

  public Page<MasterProjectDTO> findByProjectName(String search, Pageable pageable) {
    Page<Object[]> result = projectRepository.findByProjectName(search, pageable);

    return result.map(obj -> {
        Project project = (Project) obj[0];
        String companyName = (String) obj[1];
        Company company = new Company();
        company.setCompanyName(companyName);

        return new MasterProjectDTO(project, company);
    });
  }

  public DetailMasterProjectDTO getDetailMasterProject(Long id, Pageable pageable) {
    try {
      Project project = getProject(id, ConstantMessage.PROJECT_NOT_FOUND);
      CategoryCode projectType = getCategoryCode(project.getProjectType().getCategoryCodeId(), ConstantMessage.PROJECT_TYPE_NOT_FOUND);
      var o = userUtilizationRepository.findByProjectIdPage(project, pageable);
      return DetailMasterProjectDTO.builder()
                                   .projectId(project.getProjectId())
                                   .companyName(project.getProjectName())
                                   .picProjectName(project.getPicProjectName())
                                   .picProjectPhone(project.getPicProjectPhone())
                                   .description(project.getDescription())
                                   .startDate(project.getStartDate())
                                   .endDate(project.getEndDate())
                                   .isActive(project.isActive())
                                   .initialProject(project.getInitialProject())
                                   .projectName(project.getProjectName())
                                   .projectTypeName(projectType.getCodeName())
                                   .teamMember(convertDetailMasterProjectTeamMemberDTO(o))
                                   .build();
    } catch (Exception ex) {
      log.info(ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }     

  }

  public List<DetailMasterProjectTeamMemberDTO> convertDetailMasterProjectTeamMemberDTO (Page<UserUtilization> userUtilizations){
    return  userUtilizations.getContent().stream()
                                         .map(this::convertTeamMemberDTO)
                                         .collect(Collectors.toList());
    
  }

  private DetailMasterProjectTeamMemberDTO convertTeamMemberDTO(UserUtilization userUtilizations) {
    return DetailMasterProjectTeamMemberDTO.builder()
                                           .userId(userUtilizations.getUserId().getUserId())
                                           .position(userUtilizations.getUserId().getPosition().getCodeName())
                                           .nip(userUtilizations.getUserId().getNip())                        
                                           .assignment(userUtilizations.getUserId().getStatusOnsite().getCodeName())
                                           .firstName(userUtilizations.getUserId().getFirstName())
                                           .lastName(userUtilizations.getUserId().getLastName())
                                           .isActive(userUtilizations.getUserId().isActive())
                                           .joinDate(userUtilizations.getStartDate())
                                           .endDate(userUtilizations.getEndDate())
                                           .build();
  }

  public AddProjectDTO buildProjectDTO(Project project) {
      CategoryCode categoryCode = categoryCodeRepository.findById(project.getProjectType().getCategoryCodeId()).orElseThrow(() -> new EntityNotFoundException(ConstantMessage.PROJECT_TYPE_NOT_FOUND));
      List<UserUtilization> userUtilizations = userUtilizationRepository.findByProjectIdList(project); 
      Long projectType;
      String projectName = "";

      projectType = categoryCode.getCategoryCodeId();
      projectName = categoryCode.getCodeName();
       
      return AddProjectDTO.builder()
                          .projectId(project.getProjectId())
                          .createdBy(project.getCreatedBy())
                          .companyId(project.getCompanyId().getCompanyId())
                          .companyName(project.getCompanyId().getCompanyName())
                          .picProjectName(project.getPicProjectName())
                          .picProjectPhone(project.getPicProjectPhone())
                          .description(project.getDescription())
                          .startDate(project.getStartDate())
                          .endDate(project.getEndDate())
                          .projectType(projectType)
                          .projectTypeName(projectName)
                          .isActive(project.isActive())
                          .initialProject(project.getInitialProject())
                          .projectName(project.getProjectName())
                          .lastModifiedBy(project.getLastModifiedBy())
                          .teamMember(userUtilizations.stream().map(this::aMemberDTO).collect(Collectors.toList()))            
                          .build();
  }

  private AddProjectTeamMemberDTO aMemberDTO (UserUtilization userUtilization){
    return AddProjectTeamMemberDTO.builder()
                                  .userId(userUtilization.getUserId().getUserId())
                                  .roleProjectId(userUtilization.getRoleProjectId().getCategoryCodeId())
                                  .roleProject(userUtilization.getRoleProjectId().getCodeName())
                                  .joinDate(userUtilization.getStartDate())
                                  .endDate(userUtilization.getEndDate())
                                  .isActive(userUtilization.isActive())
                                  .build();
  }

  public ResponseEntity<Object> getStatus(Exception ex) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(status)
                         .body(JsonApiError.create()
                         .withStatus(status.series().name() + ":" + status.value() + ":" + status.name())
                         .withTitle(status.name())
                         .withDetail(ex.getMessage()));
  }

  private Users getUser(Long id, String message) {
    return userRepository.findByIdAndActive(id).orElseThrow(() -> new EntityNotFoundException(message));
  }

  private Company getCompany(Long id, String message) {
    return companyRepository.findByIdAndActive(id).orElseThrow(() -> new EntityNotFoundException(message));
  }

  private Project getProject(Long id, String message) {
    return projectRepository.findByIdAndActive(id).orElseThrow(() -> new EntityNotFoundException(message));
  }

  private CategoryCode getCategoryCode(Long id, String message) {
    return categoryCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(message));    
  }
}