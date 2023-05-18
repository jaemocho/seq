package com.common.seq.common;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.common.seq.common.Constants.ExceptionClass;
import com.common.seq.common.exception.ShopException;

@Component
public class CommonUtils {
    private static final String NOT_FOUND = "Not Found ";
    
    public static void nullCheckAndThrowException(Object object, String className){
        if(object == null) {
            throw new ShopException(ExceptionClass.SHOP
            , HttpStatus.BAD_REQUEST, NOT_FOUND +" "+ className); 
        }
    }
}
