package com.common.seq.service.id;

public interface IDHandler {
    
    ID genNewID();

    Boolean validate(ID id);
    
    ID update(ID id, Object... param);

    
}
