package com.common.seq.data.dto.shop;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ReqItemDto {
    
    private Long id;

    private String name;
    
    private int price;

    private int remainQty;

    private Long categoryId;
}
