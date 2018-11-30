package com.sdk4.boot.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页查询结果
 */
@NoArgsConstructor
@Data
public class PageResult<T> {

    /**
     * 总记录数 Page.totalElements
     */
    private long total;

    /**
     * 当前页码 Page.number
     */
    private int pageIndex;

    /**
     * 每页显示记录数 Page.size
     */
    private int pageSize;

    /**
     * 总页数 Page.totalPages
     */
    private int pageCount;

    private List<T> data;

    public static <T> PageResult<T> by(Page<T> page) {
        PageResult<T> result = new PageResult<>();

        result.total = page.getTotalElements();
        result.pageIndex = page.getNumber() + 1;
        result.pageSize = page.getSize();
        result.pageCount = page.getTotalPages();
        result.data = page.getContent();

        return result;
    }

    public void putPageExcludeData(Page page) {
        this.total = page.getTotalElements();
        this.pageIndex = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.pageCount = page.getTotalPages();
    }
}
