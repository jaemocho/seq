package com.common.seq.data.dto.shop;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RespCategoryDto {
    
    private Long id;

    private String name;
}
