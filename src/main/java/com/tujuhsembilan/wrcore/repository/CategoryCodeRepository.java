package com.tujuhsembilan.wrcore.repository;

import com.tujuhsembilan.wrcore.model.CategoryCode;
import com.tujuhsembilan.wrcore.ol.dto.CategoryCodeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryCodeRepository extends JpaRepository<CategoryCode, Long> {

  @Query("SELECT new com.tujuhsembilan.wrcore.ol.dto.CategoryCodeDTO(cc) FROM CategoryCode cc " +
      "WHERE cc.categoryName LIKE :categoryName " +
      "AND (:search IS NULL OR LOWER(cc.codeName) LIKE %:search%)")
  List<CategoryCodeDTO> findAllByCategoryNameAndSearch(String categoryName, String search);

  @Query("SELECT cc FROM CategoryCode cc " +
         "WHERE cc.categoryName =:categoryName " +
         "AND cc.categoryCodeId =:id")
  Optional<CategoryCode> findRoleProjectId(String categoryName, Long id);  

  Optional<CategoryCode> findByCategoryCodeId(Long categoryCodeId);
}
