package com.common.seq.data.dao.shop;

import java.util.List;

import com.common.seq.data.entity.shop.Category;

public interface CategoryDAO {
    
    public Category save(Category category);

    public List<Category> getAllCategory();
}
