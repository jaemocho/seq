package com.common.seq.data.dto.shop;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RespMemberDto {
    
    private String id;

    private String address;

    private String phoneNumber;

}
