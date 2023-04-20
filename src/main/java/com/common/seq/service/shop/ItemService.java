package com.common.seq.service.shop;

import java.util.List;

import com.common.seq.data.dto.shop.ReqItemDto;
import com.common.seq.data.dto.shop.RespItemDto;
import com.common.seq.data.entity.shop.Item;

public interface ItemService {
    
    public Item addItem(ReqItemDto reqItemDto);

    public void removeItem(Long id);
    
    public List<RespItemDto> getAllItem();

    public List<RespItemDto> getItemByCategoryId(Long id);

    public void updateItem(Long id, ReqItemDto reqItemDto);
}
