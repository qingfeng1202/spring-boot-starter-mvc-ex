package com.mvc.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class ResultData implements Serializable {

    private Long total;

    private Integer pageNum;

    private Integer pages;

    private Collection list;

    public ResultData() {
        this.total = 0L;
        this.pageNum = 1;
        this.pages = 0;
        this.list = new ArrayList<>();
    }

    /**
     * @param total
     * @param pageNum
     * @param pages
     * @param list
     */
    public ResultData(Long total, Integer pageNum, Integer pages, Collection list) {
        this.total = total;
        this.pageNum = pageNum;
        this.pages = pages;
        this.list = list;
    }

}
