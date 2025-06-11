package com.weiwei.wang.common.domain.response;

import com.weiwei.wang.common.domain.request.PageRequest;

import java.util.Collections;
import java.util.List;

public class PageResponse<T> {


    private Long totalRecords;

    private List<T> rows;

    private Integer pageIndex;

    private Integer pageSize;

    public static PageResponse fromPageRequest(PageRequest request) {
        PageResponse pageResponse = new PageResponse<>();
        pageResponse.setPageIndex(request.getPageIndex());
        pageResponse.setPageSize(request.getPageSize());
        return pageResponse;
    }

    public static PageResponse fromPageRequestAndEmptyList(PageRequest request) {
        PageResponse pageResponse = new PageResponse<>();
        pageResponse.setPageIndex(request.getPageIndex());
        pageResponse.setPageSize(request.getPageSize());
        pageResponse.setRows(Collections.emptyList());
        pageResponse.setTotalRecords(0L);
        return pageResponse;
    }


    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageResponse{" +
                "totalRecords=" + totalRecords +
                ", rows=" + rows +
                ", pageIndex=" + pageIndex +
                ", pageSize=" + pageSize +
                '}';
    }
}
