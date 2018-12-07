package com.sdk4.boot.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页数据返回
 *
 * @author sh
 */
@Data
@NoArgsConstructor
public class PageResponse<T> extends BaseResponse<List<T>> {
    private Integer total;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer pageCount;

    public PageResponse(Page<T> page) {
        this.total = (int) page.getTotalElements();
        this.pageIndex = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.pageCount = page.getTotalPages();
        this.data = page.getContent();
    }

    public PageResponse(Page page, List<T> data) {
        this.total = (int) page.getTotalElements();
        this.pageIndex = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.pageCount = page.getTotalPages();
        this.data = data;
    }
}
