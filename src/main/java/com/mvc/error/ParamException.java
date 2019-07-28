package com.mvc.error;

import lombok.Getter;

@Getter
public class ParamException extends WebException {

    public ParamException(Integer code, String message){
        super(code, message);
    }
}
