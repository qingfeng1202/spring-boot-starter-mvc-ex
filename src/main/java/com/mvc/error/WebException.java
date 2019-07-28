package com.mvc.error;

import lombok.Getter;

@Getter
public class WebException extends RuntimeException {

    private Integer code;

    public WebException(Integer code, String message){
        super(message);
        this.code = code;
    }
}
