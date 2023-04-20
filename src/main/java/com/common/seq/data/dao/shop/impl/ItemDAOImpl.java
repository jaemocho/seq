package com.common.seq.data.dao.shop.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.common.seq.data.dao.shop.ItemDAO;
import com.common.seq.data.entity.shop.Item;
import com.common.seq.data.repository.shop.ItemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemDAOImpl implements ItemDAO{
    
    private final ItemRepository itemRepository;

    public Item save(Item item){
        return itemRepository.save(item);
    }

    public void delete(Item item) {
        itemRepository.delete(item);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public List<Item> findByCategoryId(Long id){
        return itemRepository.findByCategoryId(id);
    }

    public Item findByIdForUpdate(Long id){
        return itemRepository.findByIdForUpdate(id);
    }

}
