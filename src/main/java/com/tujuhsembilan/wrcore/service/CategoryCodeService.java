package com.tujuhsembilan.wrcore.service;

import com.tujuhsembilan.wrcore.ol.dto.CategoryCodeDTO;
import com.tujuhsembilan.wrcore.repository.CategoryCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CategoryCodeService {

    private final CategoryCodeRepository categoryCodeRepository;

    public List<CategoryCodeDTO> findAllByCategoryNameAndSearch(String categoryName,String search){
        return categoryCodeRepository.findAllByCategoryNameAndSearch(categoryName, search.toLowerCase());
    }


}
