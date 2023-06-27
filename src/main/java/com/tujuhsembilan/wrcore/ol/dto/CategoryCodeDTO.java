package com.tujuhsembilan.wrcore.ol.dto;

import com.toedter.spring.hateoas.jsonapi.JsonApiId;
import com.toedter.spring.hateoas.jsonapi.JsonApiType;
import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import com.tujuhsembilan.wrcore.model.CategoryCode;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonApiTypeForClass("OptionList")
public class CategoryCodeDTO {

    private Long id;
    private String name;

//    @JsonApiType
//    public Long getCategoryId()
//    {
//        return this.id;
//    }
    public CategoryCodeDTO(CategoryCode cc){
        this.id = cc.getCategoryCodeId();
        this.name = cc.getCodeName();
    }

}
