package com.common.seq.id;

public interface IDHandler {
    
    ID genNewID();

    Boolean validate(ID id);
    
    ID update(ID id, Object... param);

    
}
