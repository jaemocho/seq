package com.common.seq.service.shop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.common.seq.data.dao.shop.CategoryDAO;
import com.common.seq.data.dto.shop.ReqCategoryDto;
import com.common.seq.data.dto.shop.RespCategoryDto;
import com.common.seq.data.entity.shop.Category;
import com.common.seq.service.shop.impl.CategoryServiceImpl;

@ExtendWith(SpringExtension.class)
public class CategoryServiceUnitTest {
    
    @Mock
    private CategoryDAO categoryDAO;
    
    @InjectMocks
	private CategoryServiceImpl categoryService;

    @Test
    public void addCategory_test() {

        //given
        ReqCategoryDto reqCategoryDto = ReqCategoryDto.builder()
                                            .name("WOMEN")
                                            .build();
        Category category = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        when(categoryDAO.save(category)).thenReturn(category);
        when(categoryDAO.findById(category.getId())).thenReturn(category);

        //when
        categoryService.addCategory(reqCategoryDto);

        //then 
        RespCategoryDto respCategoryDto = categoryService.getCategoryById(category.getId());
        assertEquals("WOMEN", respCategoryDto.getName());
    }

    @Test
    public void getAllCategory_test() {

        //given
        Category category1 = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        Category category2 = Category.builder()
                                .id(2L)
                                .name("MEN")
                                .build();

        Category category3 = Category.builder()
                                .id(3L)
                                .name("KIDS")
                                .build();

        List<Category> categoryList = new ArrayList<Category>();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        when(categoryDAO.findAll()).thenReturn(categoryList);
        when(categoryDAO.findById(1l)).thenReturn(category1);
        
        //when
        List<RespCategoryDto> respCategoryDtos = categoryService.getAllCategory();
        RespCategoryDto respCategoryDto = categoryService.getCategoryById(1L);

        //then 
        assertEquals(3, respCategoryDtos.size());
        assertEquals("WOMEN", respCategoryDto.getName());
    }

    @Test
    public void getCategoryByName_test() {

        //given
        Category category1 = Category.builder()
                                .id(1L)
                                .name("WOMEN")
                                .build();

        Category category2 = Category.builder()
                                .id(2L)
                                .name("MEN")
                                .build();

        Category category3 = Category.builder()
                                .id(3L)
                                .name("KIDS")
                                .build();
        List<Category> categoryList = new ArrayList<Category>();
        categoryList.add(category1);
        categoryList.add(category2);
        categoryList.add(category3);

        when(categoryDAO.findByName("WOMEN")).thenReturn(categoryList);
        
        //when
        List<RespCategoryDto> respCategoryDtos = categoryService.getCategoryByName("WOMEN");

        //then 
        assertEquals(3, respCategoryDtos.size());
    }



}
