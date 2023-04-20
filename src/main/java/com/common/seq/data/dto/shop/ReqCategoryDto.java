package com.common.seq.data.dto.shop;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqCategoryDto {
    
    private Long id;

    @NotNull
    private String name;

}
