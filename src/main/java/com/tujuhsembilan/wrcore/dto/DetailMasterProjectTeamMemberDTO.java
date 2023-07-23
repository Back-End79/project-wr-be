package com.tujuhsembilan.wrcore.dto;
import java.util.Date;

import javax.persistence.Id;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Detail Master Project")
public class DetailMasterProjectTeamMemberDTO {
    @Id
    private Long userId;
    private String nip;
    private String firstName;
    private String lastName;
    private String position;    
    private boolean isActive;
    private String assignment;
    private Date joinDate;
    private Date endDate;    
}
