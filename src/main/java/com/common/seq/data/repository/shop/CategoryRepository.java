package com.common.seq.data.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.common.seq.data.entity.shop.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

    public List<Category> findByName(String name);

}