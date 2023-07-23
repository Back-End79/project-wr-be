package com.tujuhsembilan.wrcore.dto;
import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("List Privilege")
public class ListDetailPrivilegeDTO {
    @Id
    private Long rolePrivilegeId;
    private Long privilegeId;
    private String privilegeName;
}
