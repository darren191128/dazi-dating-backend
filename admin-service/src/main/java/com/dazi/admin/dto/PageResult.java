package com.dazi.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    
    private List<T> list;
    private Long total;
    private Integer page;
    private Integer pageSize;
    private Integer totalPages;
    
    public PageResult(List<T> list, Long total, Integer page, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }
}
