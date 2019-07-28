package com.mvc.pojo;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class ErrorLog {

    public ErrorLog() {
    }

    public ErrorLog(Exception e, HttpServletRequest request) {
        this.e = e;
        this.request = request;
    }

    private Exception e;

    private HttpServletRequest request;

}
