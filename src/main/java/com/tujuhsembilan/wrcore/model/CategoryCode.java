package com.tujuhsembilan.wrcore.model;

import com.toedter.spring.hateoas.jsonapi.JsonApiTypeForClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "category_code", schema = "public")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_code_id", unique = true, nullable = false)
    private Long categoryCodeId;

    @Column(name = "category_name", length = 100)
    private String categoryName;

    @Column(name = "code_name", length = 100)
    private String codeName;
}
