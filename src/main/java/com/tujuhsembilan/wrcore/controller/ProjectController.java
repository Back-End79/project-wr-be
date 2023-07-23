package com.tujuhsembilan.wrcore.controller;

import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.dto.AddProjectDTO;
import com.tujuhsembilan.wrcore.dto.DetailMasterProjectDTO;
import com.tujuhsembilan.wrcore.dto.MasterProjectDTO;
import com.tujuhsembilan.wrcore.dto.ProjectDTO;
import com.tujuhsembilan.wrcore.model.Project;
import com.tujuhsembilan.wrcore.repository.ProjectRepository;
import com.tujuhsembilan.wrcore.repository.UserUtilizationRepository;
import com.tujuhsembilan.wrcore.service.ProjectService;
import com.tujuhsembilan.wrcore.util.constant.ConstantMessage;
import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;


@Slf4j
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectController {

    private final ProjectService projectService;
    private final MessageUtil msg;
    private final ProjectRepository projectRepository;
    private final UserUtilizationRepository userUtilizationRepository;

   @GetMapping
    public ResponseEntity<Object> getAllProject(@RequestParam String search, Pageable pageable) {
        Page<MasterProjectDTO> project = projectService.findByProjectName(search, pageable);
        List<MasterProjectDTO> projectDTO = new ArrayList<>(project.getContent());
        return ResponseEntity.ok(PagedModel.of(projectDTO,
        new PageMetadata(project.getSize(), project.getNumber(),
            project.getTotalElements())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDetailProject(@PathVariable Long id, Pageable pageable) {
      try {     
        DetailMasterProjectDTO projectDTO = projectService.getDetailMasterProject(id, pageable);
        Project project = projectRepository.findByIdAndActive(id).orElseThrow(() -> new EntityNotFoundException(ConstantMessage.PROJECT_NOT_FOUND));
        var o = userUtilizationRepository.findByProjectIdPage(project, pageable);
        var p = EntityModel.of(projectDTO);
        return ResponseEntity.ok()
                              .body(JsonApiModelBuilder
                              .jsonApiModel()
                              .model(p)
                              .meta("page", new PageMetadata(o.getSize(), o.getNumber(),
            o.getTotalElements()))                            
                              .build());       
      } catch (Exception ex) {
        log.info(ex.getMessage());
        ex.printStackTrace();        
        return projectService.getStatus(ex);
      }      
    }
    
    @PostMapping("/addProject")
    public ResponseEntity<Object> addProject(@RequestBody AddProjectDTO addProjectDTO) {
      try{
         var o = projectService.addProject(addProjectDTO);
         var c = projectService.buildProjectDTO(o);
         return ResponseEntity.status(HttpStatus.CREATED)
                              .body(JsonApiModelBuilder
                              .jsonApiModel()
                              .model(EntityModel.of(c))
                              .meta(ConstantMessage.MESSAGE,msg
                              .get("application.success.created", "Project Data"))
                              .build());
      } catch (Exception ex){
        log.info(ex.getMessage());
        ex.printStackTrace();
        return projectService.getStatus(ex);
      }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateProject(@PathVariable("id") Long id, @RequestBody AddProjectDTO projectDTO) {
      try{
        var o = projectService.updateProject(id, projectDTO);
        var c = projectService.buildProjectDTO(o);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(JsonApiModelBuilder
                             .jsonApiModel()
                             .model(EntityModel.of(c))
                             .meta(ConstantMessage.MESSAGE,msg
                             .get("application.success.updated"))
                             .build());
      } catch (Exception ex){
        log.info(ex.getMessage());
        ex.printStackTrace();        
        return projectService.getStatus(ex);
      }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProject(@PathVariable("id") Long id, @RequestBody ProjectDTO projectDTO) {
      try{
        projectService.deleteProject(id, projectDTO);        
        return ResponseEntity.status(HttpStatus.CREATED)
                              .body(JsonApiModelBuilder
                              .jsonApiModel()
                              .meta(ConstantMessage.MESSAGE, msg
                              .get("application.success.deleted"))
                              .build());
     } catch (Exception ex){
        log.info(ex.getMessage());
        ex.printStackTrace();        
        return projectService.getStatus(ex);
     }
    }     
}
