package com.common.seq.service.shop.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.common.Constants.ExceptionClass;
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
        
        Category category = Category.builder()
                            .name(reqCategoryDto.getName())
                            .build();
        
        categoryDAO.save(category);

    }

    @Transactional
    public void removeCategory(Long id) throws ShopException{

        Category category = categoryDAO.findById(id);
        
        if( category == null) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, "Not Found Category"); 
        }

        // item 삭제를 해야하나 ?

        categoryDAO.delete(category);
    }

    @Transactional(readOnly = true)
    public List<RespCategoryDto> getAllCategory() {
        
        List<Category> categorys = categoryDAO.findAll();

        List<RespCategoryDto> respCategoryDtos = new ArrayList<RespCategoryDto>();

        for (Category c : categorys) {
            respCategoryDtos.add(entityToRespDto(c));
        }
        return respCategoryDtos;

    }

    @Transactional(readOnly = true)
    public List<RespCategoryDto> getCategoryByName(String name) {
        
        List<Category> categorys = categoryDAO.findByName(name);

        List<RespCategoryDto> respCategoryDtos = new ArrayList<RespCategoryDto>();

        for (Category c : categorys) {
            respCategoryDtos.add(entityToRespDto(c));
        }
        return respCategoryDtos;
    }

    @Transactional(readOnly = true)
    public RespCategoryDto getCategoryById(Long id) throws ShopException {

        Category category = categoryDAO.findById(id);

        if( category == null) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, "Not Found Category"); 
        }

        return entityToRespDto(category);
    }

    private RespCategoryDto entityToRespDto(Category c) {

        if ( c == null ) return null;

        return RespCategoryDto.builder()
                                .id(c.getId())
                                .name(c.getName())
                                .build();
    }
}
