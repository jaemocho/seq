package com.common.seq.service.shop.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.data.dao.shop.CategoryDAO;
import com.common.seq.data.dao.shop.ItemDAO;
import com.common.seq.data.dto.shop.ReqItemDto;
import com.common.seq.data.dto.shop.RespItemDto;
import com.common.seq.data.entity.shop.Category;
import com.common.seq.data.entity.shop.Item;
import com.common.seq.service.shop.ItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;

    private final CategoryDAO categoryDAO;

    @Transactional
    public Item addItem(ReqItemDto reqItemDto) {
        
        Item item = Item.builder()
                        .name(reqItemDto.getName())
                        .price(reqItemDto.getPrice())
                        .remainQty(reqItemDto.getRemainQty())
                        .build();

        Category category = categoryDAO.findById(reqItemDto.getCategoryId());

        if( category == null) {
            // exception category 등록을 안해도되긴 하지 
        }

        item.setCategory(category);

        return item;
    }

    @Transactional
    public void removeItem(Long id) {

        Item item = itemDAO.findById(id);
        
        if( item == null) {
            // exception
        }

        itemDAO.delete(item);
    }

    @Transactional(readOnly = true)
    public List<RespItemDto> getAllItem() {
        
        List<Item> items = itemDAO.findAll();

        List<RespItemDto> respItemDtos = new ArrayList<RespItemDto>();

        for (Item i : items) {
            respItemDtos.add(entityToRespDto(i));
        }
        return respItemDtos;
    }

    @Transactional(readOnly = true)
    public RespItemDto getItemById(Long id) {
        
        Item item = itemDAO.findById(id);
        
        return entityToRespDto(item);
    }


    @Transactional(readOnly = true)
    public List<RespItemDto> getItemByCategoryId(Long id) {
        
        List<Item> items = itemDAO.findByCategoryId(id);

        List<RespItemDto> respItemDtos = new ArrayList<RespItemDto>();

        for (Item i : items) {

            respItemDtos.add(entityToRespDto(i));
        }
        return respItemDtos;
    }

    @Transactional
    public void updateItem(Long id, ReqItemDto reqItemDto) {
        
        Item item = itemDAO.findByIdForUpdate(id);

        if(item == null) {
            // exception 
            return;
        }

        item.updateItem(reqItemDto.getName(), reqItemDto.getPrice(), reqItemDto.getRemainQty());
    }

    private RespItemDto entityToRespDto(Item i) {

        Category category = i.getCategory();
        Long categoryId = -1L;
        String categoryName = "";

        if ( category != null) {
            categoryId = category.getId();
            categoryName = category.getName();
        }

        return RespItemDto.builder()
                        .name(i.getName())
                        .price(i.getPrice())
                        .remainQty(i.getRemainQty())
                        .categoryId(categoryId)
                        .categoryName(categoryName)
                        .build();
    }
}
