package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroupParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author jhmarryme.cn
 * @date 2019/5/26 16:58
 */
@RequestMapping("spec")
public interface SpecificationApi {
    /**
     * 根据分组id查询规格组参数
     * @param gid 组id
     * @return 商品规格参数数组
     */
    @GetMapping("params")
    List<SpecGroupParam> queryParams(@RequestParam(value = "gid", required = false) Long gid,
                                    @RequestParam(value = "cid", required = false) Long cid,
                                    @RequestParam(value = "searching", required = false) Boolean searching,
                                    @RequestParam(value = "generic", required = false) Boolean generic
    );
}
