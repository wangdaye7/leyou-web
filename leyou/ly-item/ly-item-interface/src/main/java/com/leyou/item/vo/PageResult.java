package com.leyou.item.vo;

import lombok.Data;

import java.util.List;

/**
 * view object  封装分页数据
 * @param <T>
 * @author jhmarryme.cn
 * @date 2019/5/17 14:23
 */

@Data
public class PageResult<T> {

    private Long total;// 总条数
    private Long totalPage;// 总页数
    private List<T> items;// 当前页数据

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}
