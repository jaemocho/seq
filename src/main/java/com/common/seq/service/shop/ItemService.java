package com.common.seq.service.shop;

import java.util.List;

import com.common.seq.common.exception.ShopException;
import com.common.seq.data.dto.shop.ReqItemDto;
import com.common.seq.data.dto.shop.RespItemDto;
import com.common.seq.data.entity.shop.Item;

public interface ItemService {
    
    public Item addItem(ReqItemDto reqItemDto);

    public void removeItem(Long id) throws ShopException;
    
    public List<RespItemDto> getAllItem();

    public List<RespItemDto> getItemByCategoryId(Long id);

    public RespItemDto getItemById(Long id) throws ShopException;

    public void updateItem(Long id, ReqItemDto reqItemDto) throws ShopException;

    public Item getItem(Long id);

    public Item getItemForUpdate(Long id);

    public void itemNullCheck(Item item);
}
