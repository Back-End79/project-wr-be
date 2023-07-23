package com.tujuhsembilan.wrcore.ol.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("OptionList")
public class UserListDTO {
    @Id
    private Long userId;
    private String fullName;
    private String groupName;

    public UserListDTO(Users users) {
        this.userId = users.getUserId();
        this.fullName = users.getFirstName() + " " + users.getLastName();
        this.groupName = users.getGroup().getCodeName();
    }
}

