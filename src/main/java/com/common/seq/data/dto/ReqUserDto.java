package com.common.seq.data.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReqUserDto {
    
    private String email;
    
    private String pwd;
    
}
