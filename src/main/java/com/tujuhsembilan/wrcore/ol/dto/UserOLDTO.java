package com.tujuhsembilan.wrcore.ol.dto;
import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("User Option List")
public class UserOLDTO {
    @Id
    private Long userId;
    private String nip;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String userName;
    
    public UserOLDTO(Users user){
        this.userId = user.getUserId();
        this.nip = user.getNip();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userName = user.getNip() + "-" + user.getFirstName() + " " + user.getLastName();
        this.isActive = user.isActive();
        
    }
}
