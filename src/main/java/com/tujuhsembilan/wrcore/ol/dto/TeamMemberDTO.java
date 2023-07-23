package com.tujuhsembilan.wrcore.ol.dto;
import javax.persistence.Id;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.Users;
import lombok.*;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("List Team Member")
public class TeamMemberDTO {
    @Id
    private Long userId;
    private Long positionId;
    private String nip;
    private String lastName;
    private String firstName;
    private String position;  
    private String assignment;  
    private boolean isActive;
    
    public TeamMemberDTO(Users user){
        this.userId = user.getUserId();
        this.nip = user.getNip();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.isActive = user.isActive();
        this.positionId = user.getPosition().getCategoryCodeId();
        this.assignment = user.getStatusOnsite().getCodeName();
        this.position = user.getPosition().getCodeName();
    }
}
