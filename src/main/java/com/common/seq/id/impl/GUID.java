package com.common.seq.id.impl;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.common.seq.id.ID;

@Component
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
