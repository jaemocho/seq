package com.common.seq.id;

import java.util.Date;

public interface ID {
    
    String getId();
    
    Date getLastModifyDate();
    
    int getLength();
    
    ID setId(String id);
    
    ID setLastModifyDate(Date lastModifyDate);
    
    ID setLength(int length);
}
