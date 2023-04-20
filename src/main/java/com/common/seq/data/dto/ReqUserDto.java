package com.common.seq.data.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqUserDto {
    
    private String email;
    
    private String pwd;
    
}
