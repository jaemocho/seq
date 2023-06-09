package com.common.seq.service.shop.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.common.CommonUtils;
import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dao.shop.CategoryDAO;
import com.common.seq.data.dto.shop.ReqCategoryDto;
import com.common.seq.data.dto.shop.RespCategoryDto;
import com.common.seq.data.entity.shop.Category;
import com.common.seq.service.shop.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    
    private final CategoryDAO categoryDAO;

    // category start
    @Transactional
    public void addCategory(ReqCategoryDto reqCategoryDto) {
        Category category = createNewCategory(reqCategoryDto);
        categoryDAO.save(category);
    }

    @Transactional
    public void removeCategory(Long id) throws ShopException{
        // item 삭제를 해야하나 ?
        Category category = getCategory(id);
        CommonUtils.nullCheckAndThrowException(category, Category.class.getName());
        categoryDAO.delete(category);
    }

    @Transactional(readOnly = true)
    public List<RespCategoryDto> getAllCategory() {
        return entityToRespDto(categoryDAO.findAll());
    }

    @Transactional(readOnly = true)
    public List<RespCategoryDto> getCategoryByName(String name) {
        return entityToRespDto(categoryDAO.findByName(name));
    }

    @Transactional(readOnly = true)
    public RespCategoryDto getCategoryById(Long id) throws ShopException {
        Category category = getCategory(id);
        CommonUtils.nullCheckAndThrowException(category, Category.class.getName());
        return entityToRespDto(category);
    }

    private Category createNewCategory(ReqCategoryDto reqCategoryDto){
        Category category = Category.builder()
                                .name(reqCategoryDto.getName())
                                .build();
        return category;                                
    }

    private Category getCategory(Long id) {
        Category category = categoryDAO.findById(id);
        return category;
    }

    private List<RespCategoryDto> entityToRespDto(List<Category> categorys){
        List<RespCategoryDto> respCategoryDtos = new ArrayList<RespCategoryDto>();

        for (Category c : categorys) {
            respCategoryDtos.add(entityToRespDto(c));
        }
        return respCategoryDtos;
    }

    private RespCategoryDto entityToRespDto(Category c) {

        if ( c == null ) return null;

        return RespCategoryDto.builder()
                                .id(c.getId())
                                .name(c.getName())
                                .build();
    }
}
