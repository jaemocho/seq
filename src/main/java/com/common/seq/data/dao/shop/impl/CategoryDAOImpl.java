package com.common.seq.data.dao.shop.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.common.seq.data.dao.shop.CategoryDAO;
import com.common.seq.data.entity.shop.Category;
import com.common.seq.data.repository.shop.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryDAOImpl implements CategoryDAO {
    
    private final CategoryRepository categoryRepository;

    public Category save(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
}
