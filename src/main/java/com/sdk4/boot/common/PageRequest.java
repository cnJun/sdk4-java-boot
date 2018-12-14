package com.sdk4.boot.common;

import com.sdk4.boot.Constants;
import lombok.Setter;

/**
 * 分页数据请求，分页从1开始
 *
 * @author sh
 */
@Setter
public class PageRequest extends BaseRequest {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer page;
    private Integer limit;

    public Integer getPageIndex() {
        if (pageIndex == null) {
            pageIndex = page;
        }
        return pageIndex == null || pageIndex < 1 ? 1 : pageIndex;
    }
    public Integer getPageSize() {
        if (pageSize == null) {
            pageSize = limit;
        }
        return pageSize == null || pageSize < 1 ? Constants.DEFAULT_PAGE_SIZE : pageSize;
    }
}
