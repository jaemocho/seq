package com.common.seq.service.shop.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.seq.common.Constants.ExceptionClass;
import com.common.seq.common.exception.ShopException;
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

        if( category != null) {
            item.setCategory(category);
        }

        itemDAO.save(item);
        
        return item;
    }

    @Transactional
    public void removeItem(Long id) throws ShopException {
        Item item = getItem(id);
        itemNullCheck(item);
        itemDAO.delete(item);
    }

    @Transactional(readOnly = true)
    public List<RespItemDto> getAllItem() {
        return entityToRespDto(itemDAO.findAll());
    }

    @Transactional(readOnly = true)
    public RespItemDto getItemById(Long id) throws ShopException {
        Item item = getItem(id);
        itemNullCheck(item);
        return entityToRespDto(item);
    }


    @Transactional(readOnly = true)
    public List<RespItemDto> getItemByCategoryId(Long id) {
        return entityToRespDto(itemDAO.findByCategoryId(id));
    }

    @Transactional
    public void updateItem(Long id, ReqItemDto reqItemDto) throws ShopException{
        Item item = getItemForUpdate(id);
        itemNullCheck(item);
        item.updateItem(reqItemDto.getName(), reqItemDto.getPrice(), reqItemDto.getRemainQty());
    }

    public Item getItem(Long id) {
        Item item = itemDAO.findById(id);        
        return item;
    }

    public Item getItemForUpdate(Long id) {
        Item item = itemDAO.findByIdForUpdate(id);
        return item;
    }

    public void itemNullCheck(Item item) {
        if( item == null) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, "Not Found Item"); 
        }
    }

    private List<RespItemDto> entityToRespDto(List<Item> items){
        
        List<RespItemDto> respItemDtos = new ArrayList<RespItemDto>();

        for (Item i : items) {

            respItemDtos.add(entityToRespDto(i));
        }

        return respItemDtos;
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
                        .id(i.getId())
                        .name(i.getName())
                        .price(i.getPrice())
                        .remainQty(i.getRemainQty())
                        .categoryId(categoryId)
                        .categoryName(categoryName)
                        .build();
    }
}
