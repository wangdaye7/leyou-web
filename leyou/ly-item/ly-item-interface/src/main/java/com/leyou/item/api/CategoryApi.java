package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/26 16:55
 */
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据多个ID查询商品分类集合
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);
}
