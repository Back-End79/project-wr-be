package com.tujuhsembilan.wrcore.dto;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryCodeDTO {
    @Id
    private Long categoryCodeId;
    private String categoryName;
    private String codeName;

}
