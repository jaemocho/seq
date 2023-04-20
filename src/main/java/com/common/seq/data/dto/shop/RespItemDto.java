package com.common.seq.data.dto.shop;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RespItemDto {
    
    private Long id;

    private String name;
    
    private int price;

    private int remainQty;

    private Long categoryId;

    private String categoryName;
}
