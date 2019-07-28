package com.mvc.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    public Result() {
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 数据内容
     */
    private Object data;

}
