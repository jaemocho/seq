package com.common.seq.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "GUID Response")
@Getter
@Setter
@Builder
public class RespGUID{
    private String guid;
    
    public String getGuid(){
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}