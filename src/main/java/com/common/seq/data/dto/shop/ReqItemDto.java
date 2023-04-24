package com.common.seq.data.dto.shop;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ReqItemDto {
    
    @NotNull
    private String name;
    
    @NotNull
    private Integer price;

    @NotNull
    private Integer remainQty;

    private Long categoryId;
}
