package com.common.seq.data.repository.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.common.seq.data.entity.shop.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

    public Category findByName(String name);
}