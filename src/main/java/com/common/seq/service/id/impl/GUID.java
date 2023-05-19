package com.common.seq.service.id.impl;

import java.util.Date;

import com.common.seq.service.id.ID;

public class GUID implements ID{

    private String id = "";

    private Date lastModifyDate ;
    
    private int length = 30;


    public String getId() {
        return id;
    }

    public ID setId(String id) {
        this.id = id;
        return this;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }
    
    public ID setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
        return this;
    }

    public int getLength() {
        return length;
    }

    public ID setLength(int length) {
        this.length = length;
        return this;
    }
    
}
