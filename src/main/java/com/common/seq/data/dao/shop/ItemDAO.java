package com.common.seq.data.dao.shop;

import java.util.List;

import com.common.seq.data.entity.shop.Item;

public interface ItemDAO {
    
    public Item save(Item item);

    public Item findById(Long id);

    public void delete(Item item);

    public List<Item> findAll();

    public List<Item> findByCategoryId(Long id);

    public Item findByIdForUpdate(Long id);
}
