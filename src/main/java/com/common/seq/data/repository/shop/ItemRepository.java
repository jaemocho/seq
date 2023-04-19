package com.common.seq.data.repository.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.common.seq.data.entity.shop.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{
    
    public List<Item> findByName(String name);

    @Query(value = "SELECT i from Item i join fetch i.category c WHERE c.id = :categoryId")
    public List<Item> findByCategoryId(Long categoryId);

    public List<Item> findByPriceGreaterThan(int price);

    public List<Item> findByRemainQtyGreaterThan(int remainQty);

}
