package com.tujuhsembilan.wrcore.ol.controller;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Pageable;
import com.toedter.spring.hateoas.jsonapi.JsonApiModelBuilder;
import com.tujuhsembilan.wrcore.ol.dto.TeamMemberDTO;
import com.tujuhsembilan.wrcore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ol/teamMember")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamMemberOLController {
    private final UserService userService;    

    @GetMapping
     public ResponseEntity<?> getTeamMemberOL (@RequestParam String search, Pageable pageable) {       
       var o = userService.getTeamMember(pageable, search);
       List<TeamMemberDTO> teamMemberDTO = o.getContent().stream().collect(Collectors.toList());
       return ResponseEntity.ok(PagedModel.of(teamMemberDTO, new PageMetadata(o.getSize(), o.getNumber(), o.getTotalElements())));                                          
     }    
}
