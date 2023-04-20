package com.common.seq.data.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqGUID {
    private String guid;
    private String fromServer;

    public String getGuid(){
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getFromServer(){
        return fromServer;
    }

    public void setFromServer(String fromServer) {
        this.fromServer = fromServer;
    }
}