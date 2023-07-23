package com.tujuhsembilan.wrcore.ol.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tujuhsembilan.wrcore.service.UserService;
import lombok.*;


@RestController
@RequestMapping("/ol/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersOLController {

    private final UserService userService;

    @GetMapping
     public ResponseEntity<?> getUserOL (@RequestParam String search){
       var o = userService.getUserOL(search);
       return ResponseEntity.ok(CollectionModel.of(o));  
     }
}
