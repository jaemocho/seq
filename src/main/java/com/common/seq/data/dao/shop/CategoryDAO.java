package com.common.seq.data.dao.shop;

import java.util.List;

import com.common.seq.data.entity.shop.Category;

public interface CategoryDAO {
    
    public Category save(Category category);

    public List<Category> findAll();

    public List<Category> findByName(String name);

    public Category findById(Long id);

    public void delete(Category category);
}
