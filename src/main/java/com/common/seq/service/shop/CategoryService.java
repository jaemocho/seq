package com.common.seq.service.shop;

import java.util.List;

import com.common.seq.data.dto.shop.ReqCategoryDto;
import com.common.seq.data.dto.shop.RespCategoryDto;

public interface CategoryService {
        // category
    
        public void addCategory(ReqCategoryDto reqCategoryDto);
    
        public void removeCategory(Long id);
    
        public List<RespCategoryDto> getAllCategory();
    
        public List<RespCategoryDto> getCategoryByName(String name);
    
        public RespCategoryDto getCategoryById(Long id);
}
