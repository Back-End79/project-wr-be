package com.tujuhsembilan.wrcore.dto;
import java.util.List;
import javax.persistence.Id;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonApiTypeForClass("Update User Role")
public class ResponseUpdateUserRoleDTO {    
    @Id
    private Long userId;    
    private List<DetailRoleDTO> detailRole;    
}
